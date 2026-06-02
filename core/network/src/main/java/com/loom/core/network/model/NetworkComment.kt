package com.loom.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkComment(
    val id: String,
    val author: NetworkPostAuthor,
    val parent: String? = null,
    val root: String? = null,
    val text: String,
    val depth: Int,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("is_deleted") val isDeleted: Boolean,
    val replies: List<NetworkComment> = emptyList(),
    val post: String? = null
)

@Serializable
data class NetworkCommentRequest(
    val text: String
)

@Serializable
data class NetworkCommentEnvelope(
    val response: NetworkCommentData
)

@Serializable
data class NetworkCommentData(
    val comments: NetworkCommentContent
)

@Serializable
data class NetworkCommentContent(
    val elements: List<NetworkComment>,
    val queryParams: NetworkCommentQueryParams? = null
)

@Serializable
data class NetworkCommentQueryParams(
    val cursor: String? = null
)

@Serializable
data class NetworkCommentResponse(
    val next: String?,
    val results: List<NetworkComment>
)
