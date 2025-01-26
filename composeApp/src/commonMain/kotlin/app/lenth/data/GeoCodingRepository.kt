package app.lenth.data

import androidx.compose.ui.graphics.Color
import app.lenth.data.network.models.PlacePredictionsResponse
import app.lenth.data.network.models.GeocodeResponse
import app.lenth.data.network.models.directions.DirectionsResponse
import app.lenth.domain.models.PlaceDomain
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import kotlin.io.encoding.ExperimentalEncodingApi

private const val API_KEY = "AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY"
class GeoCodingRepository(private val httpClient: HttpClient) {

    /**
     * Obtain the latitude and longitude of a place
     */
    suspend fun getGeoCoding(place: String): PlaceDomain {
        val geocodeResponse = httpClient.get(urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=$place&key=$API_KEY").body<GeocodeResponse>()

        Logger.i(tag = "this.", throwable = null, messageString = "Response: ${geocodeResponse.geocodeResults[0].geometry.location}")
        return PlaceDomain(place, geocodeResponse.geocodeResults[0].geometry.location.lat, geocodeResponse.geocodeResults[0].geometry.location.lng)
    }

    /**
     * Obtain a list of places based on the input
     */
    suspend fun getPlaceAutocompleteResults(input: String, searchTypeDomain: SearchTypeDomain): List<String> {
        Logger.i(tag = "this.", throwable = null, messageString = "Input: $input & type: ${searchTypeDomain.type}")
        try {
            val response = httpClient.get(urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$input&types=${searchTypeDomain.type}&key=$API_KEY").body<PlacePredictionsResponse>()

            val predictions = response.placePredictions.map { it.placeDescription }
            Logger.i(tag = "this.", throwable = null, messageString = "Response: $predictions")
            return predictions
        }catch (e: Exception) {
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
        // Base URL for the Static Maps API
        val baseUrl = "https://maps.googleapis.com/maps/api/staticmap"

        // val urlSafe = Base64.UrlSafe.encode("&path=enc:yo|oFhshA??ugcB{n~@k~pGajoL`x|Dvwxb@".encodeToByteArray())
        val latAverage = places.map { it.lat }.average()
        val lngAverage = places.map { it.lng }.average()
        val center = "$latAverage,$lngAverage"


        // Base64.UrlSafe.encodeToAppendable()
        // val response = httpClient.get(urlBuilder.build()) {
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
            httpClient.get(baseUrl) {
                this.parameter("size", "800x600")
                this.parameter("markers", "color:red|label:$firstPlaceLabel|${firstPlace.lat}, ${firstPlace.lng}")
                this.parameter("markers", "color:white|label:$lastPlaceLabel|${lastPlace.lat},${lastPlace.lng}")
                remainingPlaces.forEachIndexed { index, place ->
                    this.parameter("markers", "color:${availableColors[currentColorIndex]}|label:${alphabet[currentAlphabetIndex]}|${place.lat},${place.lng}")
                    if(index == alphabet.size) {
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
        }catch (e: Exception) {
            Logger.e(tag = "this.", throwable = e, messageString = "Error: $e")
            return ByteArray(0)
        }


        if(response.status.isSuccess()) {
            Logger.i(tag = "this.", throwable = null, messageString = "Response static maps: ${response.status}, ")
            Logger.i(tag = "this.", throwable = null, messageString = "URL request: ${response.request.url}, ")
            // Logger.i(tag = "this.", throwable = null, messageString = "Response text: ${response.bodyAsText()}")
            return response.readRawBytes() // Read the image data
        } else {
            Logger.e(tag = "this.", throwable = null, messageString = "Error: ${response.status} - ${response.bodyAsText()}")
            return ByteArray(0) // Return an empty array if there's an error
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

        if(response.status.isSuccess()) {
            val directionsResponse = response.body<DirectionsResponse>()
            val points =  directionsResponse.routes[0].overviewPolyline.points
            Logger.i(tag = "this.", throwable = null, messageString = "Response: $points")
            return points
        } else {
            Logger.e(tag = "this.", throwable = null, messageString = "Error: ${response.status} - ${response.bodyAsText()}")
            return null
        }
    }
}

/**
 * Convert a Color object to a valid Google Maps Static API color string.
 *
 * @param color The Color object.
 * @return A valid color string (e.g., "red", "blue", "0xff0000").
 */
fun colorToString(color: Color): String {
    return when (color) {
        Color.Red -> "red"
        Color.Blue -> "blue"
        else -> "0x${color.toString().substring(7, 9)}${color.toString().substring(9, 11)}${color.toString().substring(11, 13)}"
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
