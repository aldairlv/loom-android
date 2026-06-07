package com.loom.core.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
enum class NotificationType {
    FOLLOW, COMMENT, LIKE, REPOST, UNKNOWN
}

@Serializable
data class Notification(
    val id: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: Instant,
    val userDisplayName: String,
    val userAvatarUrl: String?,
    val actionText: String,
    val postThumbnailUrl: String? = null,
    val postContentText: String? = null
)

data class NotificationsResult(
    val notifications: List<Notification>,
    val nextCursor: String?
)
