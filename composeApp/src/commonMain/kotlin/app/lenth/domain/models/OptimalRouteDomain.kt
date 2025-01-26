package app.lenth.domain.models

class OptimalRouteDomain(
    val id: Int? = null,
    val distance: Double,
    val list: List<PlaceDomain>,
    val mapImage: ByteArray?
)

data class PlaceDomain(
    val name: String,
    val lat: Double,
    val lng: Double

)