package com.loom.core.network.model

import kotlinx.serialization.Serializable

/**
 * Legacy NetworkPost, now deprecated in favor of NetworkPostFeedItem.
 * Keeping it for compatibility while refactoring.
 */
@Serializable
data class NetworkPost(
    val id: String,
    val author: NetworkPostAuthor,
    val contents: List<NetworkPostContent>,
    val tags: List<String>,
    val status: String,
    val layout: List<NetworkLayoutRoot> = emptyList(),
    val created_at: String,
    val updated_at: String
)
