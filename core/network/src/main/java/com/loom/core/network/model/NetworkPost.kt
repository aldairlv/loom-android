package com.loom.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPost(
    val id: Long,
    val blog: Int,
    val timestamp: Long,
    val tags: List<String>,
    @SerialName("content_blocks")
    val contentBlocks: List<NetworkContentBlock>,
    @SerialName("likes_count")
    val likesCount: Int,
    @SerialName("reposts_count")
    val repostsCount: Int,
    @SerialName("comments_count")
    val commentsCount: Int,
    @SerialName("notes_count")
    val notesCount: Int,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)

@Serializable
data class NetworkContentBlock(
    val id: Int,
    val type: String, // "image" o "text"
    val order: Int,
    val text: String? = null,
    val media: List<NetworkMedia>? = null
)

@Serializable
data class NetworkMedia(
    val url: String,
    val type: String,
    val width: Int,
    val height: Int
)