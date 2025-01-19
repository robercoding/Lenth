package app.lenth.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("optimal_route")
data class OptimalRouteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "route_list") val list: List<String>,
    @ColumnInfo(name = "distance") val distance: Double,
)