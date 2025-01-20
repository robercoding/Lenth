package app.lenth.data

import app.lenth.data.network.models.LocationDomain
import app.lenth.data.network.models.PlacePredictionsResponse
import app.lenth.data.network.models.Response
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class GeoCodingRepository(private val httpClient: HttpClient) {
    suspend fun getGeoCoding(place: String): LocationDomain {
        val response = httpClient.get(urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=$place&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY").body<Response>()

        Logger.i(tag = "this.", throwable = null, messageString = "Response: ${response.results[0].geometry.location}")
        return LocationDomain(response.results[0].geometry.location.lat, response.results[0].geometry.location.lng)
    }

    suspend fun getPlacesList(input: String, searchTypeDomain: SearchTypeDomain): List<String> {
        Logger.i(tag = "this.", throwable = null, messageString = "Input: $input & type: ${searchTypeDomain.type}")
        try {
            val response = httpClient.get(urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$input&types=${searchTypeDomain.type}&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY").body<PlacePredictionsResponse>()

            val predictions = response.placePredictions.map { it.placeDescription }
            Logger.i(tag = "this.", throwable = null, messageString = "Response: $predictions")
            return predictions
        }catch (e: Exception) {
            Logger.e(tag = "this.", throwable = e, messageString = "Error: $e")
            return emptyList()
        }
    }

    suspend fun getStaticMapUrlImage() {
        val response = httpClient.get
        
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
