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
        // Base URL for the Static Maps API
        val baseUrl = "https://maps.googleapis.com/maps/api/staticmap"

        // val urlSafe = Base64.UrlSafe.encode("&path=enc:yo|oFhshA??ugcB{n~@k~pGajoL`x|Dvwxb@".encodeToByteArray())

        // Base64.UrlSafe.encodeToAppendable()
        // val response = httpClient.get(urlBuilder.build()) {
        val encodedPolyline = getEncodedPolyline(places) ?: return null
        Logger.i(tag = "this.", throwable = null, messageString = "Calling to static map now")

        val response = try {
            httpClient.get(baseUrl) {
                this.parameter("size", "800x600")
                // this.parameter("center", "39.5728,-0.3472359")
                this.parameter("path", "enc:$encodedPolyline")
                // this.parameter("path", "enc:unopFrd`AwAzKc@pCsBy@w@YWnB}@fFA?E@IFCJANBJHJH@HAHG@ElMpElNbF`@b@@JFTDF?l@Cd@SfAs@~DgDjRUjAo@lCg@tAk@pAi@~@e@t@eAnAm@l@u@l@iAr@_Ab@aBh@cFlAqBTkC`@wElAyMdDcDp@cD|@g[rHuItB_Bd@uCdAoCpAkDnByDpCwC`CkB`BuCpCuIvIg@f@wAfB]n@]r@c@pAOp@[`CGjBIjFQtAQRGVAZHXNPLDD@PNVh@L^J~@TdBJl@f@jE@VEf@GVKXQP]NW@SCQIUUQUiAkFQk@_@aAq@iA]k@Wg@sDyKgBsE_@{@[c@kDuI]w@kCaGyEsJsGuLmCkEeF{H{EyGeDeEsEmFaDqDmCoCuFoF}BqBoE{D{FmF_C}BuGgHgCwC_AeAmB_CiCiDwAsB_AyA_G_JkD_GcAiB_DaG}DgIoCgGkDuIuEiMsDkL{BeIgBeHqBqIqCgLeC{IwAqEiAcDkBwEmBoEeBqDaD}FU]AMCSk@kAi@iAk@}AW{@_AaE_C{L_DoP}EiWq@qDk@qC[iAc@eA}@qA}@_Au@m@}GwE_BgAYS[IgCeBm@c@iCeB_BkAsAgAkCaCoGiGaH}GwBoBeBkAcAg@_Bo@{Cy@uA[eS{EiM_DwEgAkHiBcCq@oBs@_A_@qBaAmOcHaGeCWKWUiBq@y@WuBi@iB_@kBQ{AIkJ]mIWqI[s@GIBkA_@m@[q@k@wA{AuAiAi@a@eAo@oA{@q@_@kDuBOKGKEIKYC]JQB[EYKOMIO?K@IFIA[G_DkFo@cA]c@iA}@AUIOMAKDEF]G_@Km@W}KoEuAk@U]BSAQGQIIMGQ?MHILE`@BXLNRDJ?DCt@Tr@VBn@K`@cCtHg@x@m@dAW`@Wl@YpBg@dE?ZMvAYpBg@xDQz@UxAe@lDtBhALDfC?XqBP@")
                this.parameter("key", API_KEY)
            }
        }catch (e: Exception) {
            Logger.e(tag = "this.", throwable = e, messageString = "Error: $e")
            return ByteArray(0)
        }


        if(response.status.isSuccess()) {
            Logger.i(tag = "this.", throwable = null, messageString = "Response static maps: ${response.status}")
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
