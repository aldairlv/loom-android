package com.loom.core.model.data

import kotlinx.datetime.Instant


data class PostFeedItem(
    val id: String,
    val author: PostAuthor,
    val parent: PostParent?,
    val root: PostParent?,
    val status: String,
    val tags: List<String>,
    val contents: List<PostFeedContent>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val publishedAt: Instant?
)

data class PostAuthor(
    val id: String,
    val displayName: String,
    val avatarUrl: String
)

data class PostParent(
    val id: String,
    val author: PostAuthor
)

data class PostFeedContent(
    val id: Int,
    val type: String, // "text" or "image"
    val order: Int,
    val text: String?,
    val media: PostMedia?
)

data class PostMedia(
    val id: String,
    val url: String,
    val type: String,
    val width: Int,
    val height: Int
)

data class PostsFeedResult(
    val posts: List<PostFeedItem>,
    val nextCursor: String?
)
