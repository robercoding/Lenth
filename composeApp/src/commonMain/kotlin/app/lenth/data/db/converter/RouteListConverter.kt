package app.lenth.data.db.converter

import androidx.room.TypeConverter

class RouteListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(";")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split(";")
    }
}