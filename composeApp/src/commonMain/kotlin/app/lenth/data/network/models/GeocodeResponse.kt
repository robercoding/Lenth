package app.lenth.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodeResponse(
    @SerialName("results")
    val geocodeResults: List<GeocodeResult>,

    @SerialName("status")
    val status: String
)

@Serializable
data class GeocodeResult(
    @SerialName("address_components")
    val addressComponents: List<AddressComponent>,

    @SerialName("formatted_address")
    val formattedAddress: String,

    @SerialName("geometry")
    val geometry: Geometry,

    @SerialName("place_id")
    val placeId: String,

    @SerialName("types")
    val types: List<String>
)

@Serializable
data class AddressComponent(
    @SerialName("long_name")
    val longName: String,

    @SerialName("short_name")
    val shortName: String,

    @SerialName("types")
    val types: List<String>
)

@Serializable
data class Geometry(
    @SerialName("bounds")
    val bounds: Bounds?,

    @SerialName("location")
    val location: Location,

    @SerialName("location_type")
    val locationType: String,

    @SerialName("viewport")
    val viewport: Bounds
)

@Serializable
data class Bounds(
    @SerialName("northeast")
    val northeast: Location,

    @SerialName("southwest")
    val southwest: Location
)

@Serializable
data class Location(
    @SerialName("lat")
    val lat: Double,

    @SerialName("lng")
    val lng: Double
)
