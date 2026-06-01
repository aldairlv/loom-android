package com.loom.core.model.data

import kotlinx.datetime.Instant

data class Comment(
    val id: String,
    val profileId: String,
    val parentId: String? = null,
    val rootId: String? = null,
    val text: String,
    val depth: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isDeleted: Boolean,
    val replies: List<Comment> = emptyList(),
    val postId: String? = null
)

data class CommentFeed(
    val nextCursor: String?,
    val comments: List<Comment>
)
