package app.lenth.ui.history.models

import app.lenth.domain.PlaceUi

// data class OptimalRouteUi(val id: Int = -1, val distance: Double, val path: List<String>) {
//     private val start = path.first()
//     private val end = path.last()
//     // private val image = "https://maps.googleapis.com/maps/api/staticmap?size=600x300&maptype=roadmap&path=color:0x0000ff|weight:5|$start|$end"
//     // private val imageStart = "https://maps.googleapis.com/maps/api/staticmap?size=600x300&maptype=roadmap&markers=color:blue|$start"
//     val imageStart = "https://maps.googleapis.com/maps/api/staticmap?center=$start&zoom=12&size=600x400&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY"
// }

data class OptimalRouteUi(
    val id: Int = -1,
    val distance: Double,
    val path: List<PlaceUi>,
    val mapImage: ByteArray?,
) {
    init {
        require(path.isNotEmpty()) { "Path must not be empty" }
    }

    private val start = path.firstOrNull() ?: PlaceUi("", 0.0, 0.0)
    private val end = path.lastOrNull() ?: PlaceUi("", 0.0, 0.0)

    // Google Maps Static API URL for the starting point
    val imageStart: String
        get() = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=${start.lat},${start.lng}&zoom=12&size=600x400&key=YOUR_API_KEY"

    // Google Maps Static API URL for the full path
    val imagePath: String
        get() {
            val pathCoordinates = path.joinToString("|") { "${it.lat},${it.lng}" }
            return "https://maps.googleapis.com/maps/api/staticmap?" +
                "size=600x400&maptype=roadmap&path=color:0x0000ff|weight:5|$pathCoordinates&key=YOUR_API_KEY"
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as OptimalRouteUi

        if (id != other.id) return false
        if (distance != other.distance) return false
        if (path != other.path) return false
        if (!mapImage.contentEquals(other.mapImage)) return false
        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + distance.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + mapImage.contentHashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }
}