package com.loom.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPostCreateRequest(
    @SerialName("parent_id") val parentId: String? = null,
    @SerialName("root_id") val rootId: String? = null,
    val status: String,
    val tags: List<String>,
    @SerialName("contents_input") val contentsInput: List<NetworkContentInput>,
    val layout: List<NetworkLayoutRoot>
)

@Serializable
data class NetworkContentInput(
    val type: String,
    val text: String? = null,
    @SerialName("media_id") val mediaId: String? = null
)

@Serializable
data class NetworkLayoutRoot(
    val type: String,
    val display: List<NetworkLayoutRow>
)

@Serializable
data class NetworkLayoutRow(
    val blocks: List<Int>
)

@Serializable
data class NetworkPostResponse(
    val id: String,
    val author: NetworkAuthor,
    val parent: NetworkPostShortResponse? = null,
    val root: NetworkPostShortResponse? = null,
    val trail: List<NetworkPostShortResponse> = emptyList(),
    val status: String,
    val tags: List<String>,
    val contents: List<NetworkPostContentResponse>,
    val layout: List<NetworkLayoutRoot>,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("published_at") val publishedAt: Instant? = null
)

@Serializable
data class NetworkAuthor(
    val id: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_url") val avatarUrl: String
)

@Serializable
data class NetworkPostShortResponse(
    val id: String,
    val author: NetworkAuthor,
    val contents: List<NetworkPostContentResponse>? = null,
    val layout: List<NetworkLayoutRoot>? = null
)

@Serializable
data class NetworkPostContentResponse(
    val id: Int,
    val type: String,
    val order: Int,
    val text: String? = null,
    val media: NetworkMedia? = null
)
