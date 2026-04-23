package com.loom.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import com.loom.core.model.data.PostContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
internal class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()

    // Converter para la lista de Tags (List<String>)
    @TypeConverter
    fun stringToList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    // Converter para los Bloques de Contenido
    // Nota: Requiere que PostContent y sus hijos sean @Serializable
    @TypeConverter
    fun contentBlocksToString(value: List<PostContent>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun stringToContentBlocks(value: String): List<PostContent> {
        return Json.decodeFromString(value)
    }
}
