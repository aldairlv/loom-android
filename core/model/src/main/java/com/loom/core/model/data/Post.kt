package com.loom.core.model.data
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val blogId: Int,
    val username: String,
    val timestamp: Long,
    val tags: List<String>,
    val likesCount: Int,
    val repostsCount: Int,
    val commentsCount: Int,
    val notesCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val content: List<PostContent>
)


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