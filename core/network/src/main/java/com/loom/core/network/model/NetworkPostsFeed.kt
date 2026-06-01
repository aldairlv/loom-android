package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NetworkPostsFeedEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkPostsFeedData
)

@Serializable
data class NetworkPostsFeedData(
    val feed: NetworkPostsFeedContent
)

@Serializable
data class NetworkPostsFeedContent(
    val elements: List<NetworkPostFeedItem>,
    @SerialName("queryParams") val queryParams: NetworkQueryParams? = null
)

@Serializable
data class NetworkQueryParams(
    val cursor: String? = null
)

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
    @SerialName("root_post")
    val root: NetworkPostParent? = null,
    val trail: List<NetworkPostFeedItem> = emptyList(),
    val status: String = "",
    val tags: List<String> = emptyList(),
    val contents: List<NetworkPostContent> = emptyList(),
    val layout: List<NetworkLayoutRoot> = emptyList(),
    val interactions: NetworkPostInteractions? = null,
    val stats: NetworkPostStats? = null,
    val is_deleted: Boolean = false,
    val created_at: String = "",
    val updated_at: String = "",
    val published_at: String? = null
)

@Serializable
data class NetworkPostAuthor(
    val id: String,
    val display_name: String,
    val avatar_url: String? = null,
    val is_followed: Boolean = false
)

@Serializable
data class NetworkPostInteractions(
    val liked: Boolean,
    val reposted: Boolean,
    val commented: Boolean
)

@Serializable
data class NetworkPostStats(
    val likes_count: Int,
    val reposts_count: Int,
    val comments_count: Int
)

@Serializable
data class NetworkPostParent(
    val id: String,
    val author: NetworkPostAuthor,
    val contents: List<NetworkPostContent>? = null,
    val layout: List<NetworkLayoutRoot>? = null
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
