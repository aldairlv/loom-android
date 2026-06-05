package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class NetworkNotificationEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkNotificationResponse
)

@Serializable
data class NetworkNotificationResponse(
    val notifications: NetworkNotificationList
)

@Serializable
data class NetworkNotificationList(
    val elements: List<NetworkNotification>,
    @SerialName("queryParams") val queryParams: NetworkQueryParams? = null
)

@Serializable
data class NetworkNotification(
    val id: String,
    @SerialName("event_type") val eventType: String, // FOLLOW, COMMENT, LIKE, REPOST
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("content_type") val contentType: String,
    @SerialName("target_id") val targetId: String,
    @SerialName("target_data") val targetData: NetworkNotificationTargetData,
    val objectType: String
)

@Serializable
data class NetworkNotificationTargetData(
    val id: String,
    // For FOLLOW
    @SerialName("from_profile") val fromProfile: NetworkNotificationProfile? = null,
    @SerialName("to_profile") val toProfile: NetworkNotificationProfile? = null,
    // For COMMENT, LIKE, REPOST
    val author: NetworkNotificationAuthor? = null,
    // For COMMENT
    val text: String? = null,
    // For LIKE, REPOST, COMMENT (Can be a string ID or a Post object)
    val post: JsonElement? = null,
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class NetworkNotificationProfile(
    val id: String,
    val username: String? = null,
    @SerialName("display_name") val displayName: String,
    val avatar: String? = null,
    @SerialName("last_posted_at") val lastPostedAt: String? = null
)

@Serializable
data class NetworkNotificationAuthor(
    val id: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("is_followed") val isFollowed: Boolean? = null
)
