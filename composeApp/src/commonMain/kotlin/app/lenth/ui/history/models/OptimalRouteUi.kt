package app.lenth.ui.history.models

import app.lenth.domain.PlaceUi


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