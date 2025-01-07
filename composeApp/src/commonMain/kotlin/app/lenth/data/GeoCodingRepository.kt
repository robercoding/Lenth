package app.lenth.data

import app.lenth.data.models.LocationDomain
import app.lenth.data.models.PlacePredictionsResponse
import app.lenth.data.models.Response
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

    suspend fun getPlacesList(input: String): List<String> {
        try {
            val response = httpClient.get(urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$input&types=geocode&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY").body<PlacePredictionsResponse>()

            val predictions = response.placePredictions.map { it.placeDescription }
            Logger.i(tag = "this.", throwable = null, messageString = "Response: $predictions")
            return predictions
        }catch (e: Exception) {
            Logger.e(tag = "this.", throwable = e, messageString = "Error: $e")
            return emptyList()
        }

    }
}

enum class SearchType(val type: String) {
    ALL("all"), // Represents no filter, includes all types
    ADDRESSES("address"), // Specific street addresses
    CITIES_TOWNS("locality"), // City or town names
    REGIONS("administrative_area_level_1"), // States, provinces, countries
    LANDMARKS("point_of_interest"), // Landmarks, parks, attractions
    BUSINESSES_PLACES("establishment"), // Commercial establishments
    POSTAL_CODES("postal_code"), // ZIP codes or postal areas
    TRANSIT_STATIONS("transit_station"), // Train, bus, and other stations
    NATURAL_FEATURES("natural_feature"), // Mountains, rivers, lakes
    NEIGHBORHOODS("neighborhood"); // Defined areas within cities

    companion object {
        fun getAllTypes(): List<String> {
            return values().map { it.type }
        }
    }
}