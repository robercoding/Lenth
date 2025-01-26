package app.lenth.data.db.converter

import androidx.room.TypeConverter
import app.lenth.data.db.models.PlaceEntity
import app.lenth.domain.models.PlaceDomain

class RouteListConverter {
    @TypeConverter
    fun fromStringList(value: List<PlaceEntity>): String {
        return value.joinToString(";") { "${it.place},${it.lat},${it.lng}" }
    }

    @TypeConverter
    fun toStringList(value: String): List<PlaceEntity> {
        return value.split(";").map {
            val (place, lat, lng) = it.split(",")
            PlaceEntity(place, lat.toDouble(), lng.toDouble())
        }
    }
}