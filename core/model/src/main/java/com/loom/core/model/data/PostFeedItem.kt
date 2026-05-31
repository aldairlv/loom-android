package com.loom.core.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PostFeedItem(
    val id: String,
    val author: PostAuthor,
    val parent: PostParent? = null,
    val root: PostParent? = null,
    val trail: List<PostFeedItem> = emptyList(),
    val status: String,
    val tags: List<String>,
    val contents: List<PostFeedContent>,
    val layout: List<LayoutRoot> = emptyList(),
    val interactions: PostInteractions? = null,
    val stats: PostStats? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
    val publishedAt: Instant? = null
)

@Serializable
data class PostAuthor(
    val id: String,
    val displayName: String,
    val avatarUrl: String,
    val isFollowed: Boolean = false
)

@Serializable
data class PostInteractions(
    val liked: Boolean,
    val reposted: Boolean,
    val commented: Boolean
)

@Serializable
data class PostStats(
    val likesCount: Int,
    val repostsCount: Int,
    val commentsCount: Int
)

@Serializable
data class PostParent(
    val id: String,
    val author: PostAuthor,
    val contents: List<PostFeedContent>? = null,
    val layout: List<LayoutRoot>? = null
)

@Serializable
data class PostFeedContent(
    val id: Int,
    val type: String, // "text" or "image" or "video"
    val order: Int,
    val text: String? = null,
    val media: PostMedia? = null
)

@Serializable
data class PostMedia(
    val id: String,
    val url: String,
    val type: String,
    val width: Int,
    val height: Int
)

@Serializable
data class LayoutRoot(
    val type: String,
    val display: List<LayoutRow>
)

@Serializable
data class LayoutRow(
    val blocks: List<Int>
)

data class PostsFeedResult(
    val posts: List<PostFeedItem>,
    val nextCursor: String?
)
