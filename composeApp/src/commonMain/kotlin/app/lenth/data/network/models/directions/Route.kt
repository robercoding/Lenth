package app.lenth.data.network.models.directions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Route(
    // val bounds: Bounds,
    // val copyrights: String,
    // val legs: List<Leg>,
    @SerialName("overview_polyline") val overviewPolyline: OverviewPolyline,
    // val summary: String,
    // val warnings: List<Any?>,
    // val waypoint_order: List<Int>
)