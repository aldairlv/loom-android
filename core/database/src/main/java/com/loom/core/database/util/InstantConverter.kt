package com.loom.core.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostParent
import com.loom.core.model.data.LayoutRoot
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()

    @TypeConverter
    fun stringToList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun authorToString(value: PostAuthor): String = Json.encodeToString(value)
    
    @TypeConverter
    fun stringToAuthor(value: String): PostAuthor = Json.decodeFromString(value)

    @TypeConverter
    fun parentToString(value: PostParent?): String? = value?.let { Json.encodeToString(it) }
    
    @TypeConverter
    fun stringToParent(value: String?): PostParent? = value?.let { Json.decodeFromString(it) }

    @TypeConverter
    fun trailToString(value: List<PostFeedItem>): String = Json.encodeToString(value)
    
    @TypeConverter
    fun stringToTrail(value: String): List<PostFeedItem> = Json.decodeFromString(value)

    @TypeConverter
    fun contentsToString(value: List<PostFeedContent>): String = Json.encodeToString(value)
    
    @TypeConverter
    fun stringToContents(value: String): List<PostFeedContent> = Json.decodeFromString(value)

    @TypeConverter
    fun layoutToString(value: List<LayoutRoot>): String = Json.encodeToString(value)
    
    @TypeConverter
    fun stringToLayout(value: String): List<LayoutRoot> = Json.decodeFromString(value)
}
