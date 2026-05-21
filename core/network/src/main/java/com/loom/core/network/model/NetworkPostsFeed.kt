package com.loom.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class NetworkPostsFeedResponse(
    val next: String?,
    val previous: String?,
    val results: List<NetworkPostFeedItem>
)

@Serializable
data class NetworkPostFeedItem(
    val id: String,
    val author: NetworkPostAuthor,
    val parent: NetworkPostParent? = null,
    val root: NetworkPostParent? = null,
    val status: String,
    val tags: List<String>,
    val contents: List<NetworkPostContent>,
    val created_at: String,
    val updated_at: String,
    val published_at: String? = null
)

@Serializable
data class NetworkPostAuthor(
    val id: String,
    val display_name: String,
    val avatar_url: String? = null
)

@Serializable
data class NetworkPostParent(
    val id: String,
    val author: NetworkPostAuthor
)

@Serializable
data class NetworkPostContent(
    val id: Int,
    val type: String,
    val order: Int,
    val text: String? = null,
    val media: NetworkPostMedia? = null
)

@Serializable
data class NetworkPostMedia(
    val id: String,
    val url: String,
    val type: String,
    val width: Int? = null,
    val height: Int? = null
)
