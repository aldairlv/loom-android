package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFollowingUsersFeedEnvelope(
    val response: NetworkFollowingUsersFeedData
)

@Serializable
data class NetworkFollowingUsersFeedData(
    val feed: NetworkFollowingUsersFeedContent
)

@Serializable
data class NetworkFollowingUsersFeedContent(
    val elements: List<NetworkFollowingUserItem>,
    @SerialName("queryParams") val queryParams: NetworkQueryParams? = null
)

@Serializable
data class NetworkFollowingUserItem(
    val id: String,
    val username: String,
    @SerialName("display_name") val displayName: String,
    val avatar: String? = null,
    @SerialName("last_posted_at") val lastPostedAt: String? = null
)

@Serializable
data class NetworkFollowingUsersFeedResponse(
    val next: String?,
    val results: List<NetworkFollowingUserItem>
)
