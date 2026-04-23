package com.loom.core.model.data
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Post(
    val id: Long,
    val blogId: Int,
    val timestamp: Long,
    val tags: List<String>,
    val contentBlocks: List<PostContent>,
    val likesCount: Int,
    val reposts_count: Int,
    val commentsCount: Int,
    val notesCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)

// En :core:model -> Post.kt
@Serializable
sealed interface PostContent {
    @Serializable
    @SerialName("text")
    data class Text(val text: String) : PostContent

    @Serializable
    @SerialName("image")
    data class Image(
        val imageUrl: String,
        val width: Int,
        val height: Int
    ) : PostContent
}