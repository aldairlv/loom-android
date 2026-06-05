package com.loom.core.data.repository

import com.loom.core.model.data.Notification
import com.loom.core.model.data.NotificationType
import com.loom.core.model.data.NotificationsResult
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkNotification
import com.loom.core.network.model.NetworkPostContentResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import javax.inject.Inject

internal class OfflineFirstNotificationsRepository @Inject constructor(
    private val network: LoomNetworkDataSource,
    private val json: Json,
): NotificationsRepository {

    override fun getNotifications(cursor: String?): Flow<NotificationsResult> = flow {
        val networkList = network.getNotifications(cursor)
        val notifications = networkList.elements.map { it.asExternalModel(json) }
        emit(NotificationsResult(notifications, networkList.queryParams?.cursor))
    }
}

private fun NetworkNotification.asExternalModel(json: Json): Notification {
    val type = when (eventType) {
        "FOLLOW" -> NotificationType.FOLLOW
        "COMMENT" -> NotificationType.COMMENT
        "LIKE" -> NotificationType.LIKE
        "REPOST" -> NotificationType.REPOST
        else -> NotificationType.UNKNOWN
    }

    val userDisplayName = targetData.fromProfile?.displayName 
        ?: targetData.author?.displayName 
        ?: "Usuario"
    
    val userAvatarUrl = targetData.fromProfile?.avatar 
        ?: targetData.author?.avatarUrl

    val actionText = when (type) {
        NotificationType.FOLLOW -> "Ahora te sigue"
        NotificationType.COMMENT -> "Ha comentado tu post"
        NotificationType.LIKE -> "Ha indicado que le ha gustado tu post"
        NotificationType.REPOST -> "Ha reposteado tu post"
        else -> ""
    }

    // Extraction of thumbnail/text from post
    var postThumbnailUrl: String? = null
    var postContentText: String? = null

    // If there is a post object in targetData
    targetData.post?.let { postElement ->
        try {
            val postObj = try {
                json.decodeFromJsonElement<NotificationPostThumb>(postElement)
            } catch (e: Exception) {
                null
            }

            postObj?.content_data?.let { contents ->
                val firstImage = contents.find { it.type == "image" || it.media != null }
                if (firstImage != null) {
                    postThumbnailUrl = firstImage.media?.url
                } else {
                    postContentText = contents.find { it.type == "text" }?.text
                }
            }
        } catch (e: Exception) {
            // Ignore parsing errors
        }
    }

    return Notification(
        id = id,
        type = type,
        isRead = isRead,
        createdAt = createdAt.toInstantOrNow(),
        userDisplayName = userDisplayName,
        userAvatarUrl = userAvatarUrl,
        actionText = actionText,
        postThumbnailUrl = postThumbnailUrl,
        postContentText = postContentText
    )
}

@kotlinx.serialization.Serializable
private data class NotificationPostThumb(
    val content_data: List<NetworkPostContentResponse>? = null
)

private fun String.toInstantOrNow(): Instant {
    return try {
        if (this.isBlank()) Clock.System.now() else Instant.parse(this)
    } catch (e: Exception) {
        Clock.System.now()
    }
}
