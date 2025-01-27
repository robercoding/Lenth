package app.lenth.data

import androidx.compose.ui.graphics.Color
import app.lenth.data.network.models.GeocodeResponse
import app.lenth.data.network.models.PlacePredictionsResponse
import app.lenth.data.network.models.directions.DirectionsResponse
import app.lenth.domain.models.PlaceDomain
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import io.ktor.http.isSuccess
import kotlin.io.encoding.ExperimentalEncodingApi

private const val API_KEY = "AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY"

class GeoCodingRepository(private val httpClient: HttpClient) {

    /**
     * Obtain the latitude and longitude of a place
     */
    suspend fun getGeoCoding(place: String): PlaceDomain {
        val baseUrl = "https://maps.googleapis.com/maps/api/geocode/json"
        // val geocodeResponse = httpClient.get(urlString = "$baseUrl?address=$place&key=$API_KEY").body<GeocodeResponse>()
        val geocodeResponse = httpClient.get(urlString = "$baseUrl") {
            this.parameter("address", place)
            this.parameter("key", API_KEY)
        }.body<GeocodeResponse>()

        Logger.i(tag = "this.", throwable = null, messageString = "Response: ${geocodeResponse.geocodeResults[0].geometry.location}")
        return PlaceDomain(place, geocodeResponse.geocodeResults[0].geometry.location.lat, geocodeResponse.geocodeResults[0].geometry.location.lng)
    }

    /**
     * Obtain a list of places based on the input
     */
    suspend fun getPlaceAutocompleteResults(input: String, searchTypeDomain: SearchTypeDomain): List<String> {
        Logger.i(tag = "this.", throwable = null, messageString = "Input: $input & type: ${searchTypeDomain.type}")
        val baseUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json"
        try {
            // val response = httpClient.get(urlString = "$baseUrl?input=$input&types=${searchTypeDomain.type}&key=$API_KEY").body<PlacePredictionsResponse>()
            val response = httpClient.get(urlString = "$baseUrl") {
                this.parameter("input", input)
                this.parameter("types", searchTypeDomain.type)
                this.parameter("key", API_KEY)
            }.body<PlacePredictionsResponse>()

            val predictions = response.placePredictions.map { it.placeDescription }
            Logger.i(tag = "this.", throwable = null, messageString = "Response: $predictions")
            return predictions
        } catch (e: Exception) {
            Logger.e(tag = "this.", throwable = e, messageString = "Error: $e")
            return emptyList()
        }
    }

    /**
     * Obtain a static map image URL
     * We need to pass the latitude and longitude of the place in order to:
     * - Obtain polyline encoded
     * - Add the markers to the map with numbers*
     */
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun getStaticMapUrlImage(
        places: List<PlaceDomain>,
    ): ByteArray? {
        val availableColors = listOf("blue", "green", "yellow", "purple", "gray", "orange", "black")
        val alphabet = ('A'..'Z').toList()

        val latAverage = places.map { it.lat }.average()
        val lngAverage = places.map { it.lng }.average()
        val center = "$latAverage,$lngAverage"

        val encodedPolyline = getEncodedPolyline(places) ?: return null
        Logger.i(tag = "this.", throwable = null, messageString = "Calling to static map now")

        val firstPlace = places.first()
        val lastPlace = places.last()
        val remainingPlaces = places.subList(1, places.size - 1)

        var currentColorIndex = 0
        var currentAlphabetIndex = 0
        val firstPlaceLabel = 0
        val lastPlaceLabel = 1
        val response = try {
            val baseUrl = "https://maps.googleapis.com/maps/api/staticmap"
            httpClient.get(baseUrl) {
                this.parameter("size", "800x600")
                this.parameter("markers", "color:red|label:$firstPlaceLabel|${firstPlace.lat}, ${firstPlace.lng}")
                this.parameter("markers", "color:red|label:$lastPlaceLabel|${lastPlace.lat},${lastPlace.lng}")
                remainingPlaces.forEachIndexed { index, place ->
                    this.parameter("markers", "color:${availableColors[currentColorIndex]}|label:${alphabet[currentAlphabetIndex]}|${place.lat},${place.lng}")
                    if (index == alphabet.size) {
                        // Reset and change color
                        currentAlphabetIndex = 0
                        currentColorIndex++
                    } else {
                        currentAlphabetIndex++
                    }
                }
                this.parameter("center", center)
                this.parameter("path", "enc:$encodedPolyline")
                this.parameter("key", API_KEY)
            }
        } catch (e: Exception) {
            Logger.e(tag = "this.", throwable = e, messageString = "Error: $e")
            return ByteArray(0)
        }


        return if (response.status.isSuccess()) {
            response.readRawBytes()
        } else {
            ByteArray(0) // Return an empty array if there's an error
        }
    }

    private suspend fun getEncodedPolyline(places: List<PlaceDomain>): String? {
        val baseurl = "https://maps.googleapis.com/maps/api/directions/json"

        val origin = "${places[0].lat},${places[0].lng}"
        val destination = "${places.last().lat},${places.last().lng}"
        val waypoints = places.subList(1, places.size - 1).joinToString(separator = "|") { "${it.lat},${it.lng}" }
        val response = httpClient.get(urlString = baseurl) {
            this.parameter("origin", origin)
            this.parameter("destination", destination)
            this.parameter("waypoints", waypoints)
            this.parameter("key", API_KEY)
        }

        if (response.status.isSuccess()) {
            val directionsResponse = response.body<DirectionsResponse>()
            val points = directionsResponse.routes[0].overviewPolyline.points
            Logger.i(tag = "this.", throwable = null, messageString = "Response: $points")
            return points
        } else {
            Logger.e(tag = "this.", throwable = null, messageString = "Error: ${response.status} - ${response.bodyAsText()}")
            return null
        }
    }
}

enum class SearchTypeDomain(val type: String) {
    ALL(""), // Represents no filter, includes all types

    // Table 3 Types
    GEOCODE("geocode"), // Geocoding results (e.g., addresses or places with coordinates)
    ADDRESSES("address"), // Specific and precise street addresses
    ESTABLISHMENTS("establishment"), // Business results
    REGIONS("(regions)"), // Broad geographic areas
    CITIES("(cities)"); // Cities or administrative areas level 3

    companion object {
        fun getAllTypes(): List<String> {
            return values().map { it.type }
        }
    }
}
