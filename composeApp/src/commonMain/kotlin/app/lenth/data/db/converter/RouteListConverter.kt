package app.lenth.data.db.converter

import androidx.room.TypeConverter
import app.lenth.data.db.models.PlaceEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RouteListConverter {
    @TypeConverter
    fun fromStringList(value: List<PlaceEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<PlaceEntity> {
        return try {

        Json.decodeFromString(value)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf(
                PlaceEntity(
                    place = "Unknown",
                    lat = 0.0,
                    lng = 0.0
                ),
                PlaceEntity(
                    place = "Unknown",
                    lat = 0.0,
                    lng = 0.0
                )
            )
        }
    }
}