package app.lenth.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity("optimal_route")
class OptimalRouteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "route_list") val list: List<PlaceEntity>,
    @ColumnInfo(name = "distance") val distance: Double,
    @ColumnInfo(name = "map_image") val mapImage: ByteArray?
)

@Serializable
data class PlaceEntity(
    val place: String,
    val lat: Double,
    val lng: Double
)