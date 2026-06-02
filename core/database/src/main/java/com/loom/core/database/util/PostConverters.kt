package com.loom.core.database.util

import androidx.room.TypeConverter
import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostParent
import com.loom.core.model.data.LayoutRoot
import com.loom.core.model.data.PostInteractions
import com.loom.core.model.data.PostStats
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromPostAuthor(value: PostAuthor): String = json.encodeToString(value)
    @TypeConverter
    fun toPostAuthor(value: String): PostAuthor = json.decodeFromString(value)

    @TypeConverter
    fun fromPostParent(value: PostParent?): String? = value?.let { json.encodeToString(it) }
    @TypeConverter
    fun toPostParent(value: String?): PostParent? = value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromPostFeedItemList(value: List<PostFeedItem>): String = json.encodeToString(value)
    @TypeConverter
    fun toPostFeedItemList(value: String): List<PostFeedItem> = json.decodeFromString(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = json.encodeToString(value)
    @TypeConverter
    fun toStringList(value: String): List<String> = json.decodeFromString(value)

    @TypeConverter
    fun fromPostFeedContentList(value: List<PostFeedContent>): String = json.encodeToString(value)
    @TypeConverter
    fun toPostFeedContentList(value: String): List<PostFeedContent> = json.decodeFromString(value)

    @TypeConverter
    fun fromLayoutRootList(value: List<LayoutRoot>): String = json.encodeToString(value)
    @TypeConverter
    fun toLayoutRootList(value: String): List<LayoutRoot> = json.decodeFromString(value)

    @TypeConverter
    fun fromPostInteractions(value: PostInteractions?): String? = value?.let { json.encodeToString(it) }
    @TypeConverter
    fun toPostInteractions(value: String?): PostInteractions? = value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromPostStats(value: PostStats?): String? = value?.let { json.encodeToString(it) }
    @TypeConverter
    fun toPostStats(value: String?): PostStats? = value?.let { json.decodeFromString(it) }
}
