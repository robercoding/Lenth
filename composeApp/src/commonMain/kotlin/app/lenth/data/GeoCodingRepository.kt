package app.lenth.data

import app.lenth.data.models.LocationDomain
import app.lenth.data.models.PredictionsResponse
import app.lenth.data.models.Response
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class GeoCodingRepository(private val httpClient: HttpClient) {
    suspend fun getGeoCoding(): LocationDomain {
        val response = httpClient.get(urlString = "https://maps.googleapis.com/maps/api/geocode/json?address=Massamagrell&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY\n").body<Response>()

        Logger.i(tag = "this.", throwable = null, messageString = "Response: ${response.results[0].geometry.location}")
        return LocationDomain(0.0, 0.0)
    }

    suspend fun getPlacesList(input: String): List<String> {
        val response = httpClient.get(urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$input&types=geocode&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY").body<PredictionsResponse>()

        val predictions = response.predictions.map { it.description }
        Logger.i(tag = "this.", throwable = null, messageString = "Response: $predictions")
        return predictions
    }
}