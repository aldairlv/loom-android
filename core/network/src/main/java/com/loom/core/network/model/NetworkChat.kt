package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkConversationEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkConversationData
)

@Serializable
data class NetworkConversationData(
    val conversation: NetworkConversation
)

@Serializable
data class NetworkConversationsEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkConversationsData
)

@Serializable
data class NetworkConversationsData(
    val conversations: NetworkConversationsContent
)

@Serializable
data class NetworkConversationsContent(
    val elements: List<NetworkConversation>,
    val queryParams: NetworkQueryParams? = null
)

@Serializable
data class NetworkConversation(
    val id: String,
    val objectType: String = "conversation",
    val type: String, // "direct" | "group"
    val name: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("display_avatar") val displayAvatar: String? = null,
    val participants: List<NetworkChatParticipant>,
    @SerialName("last_message") val lastMessage: NetworkMessage? = null,
    @SerialName("unread_count") val unreadCount: Int = 0,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)

@Serializable
data class NetworkChatParticipant(
    @SerialName("user_id") val userId: String,
    val username: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_url") val avatarUrl: String? = null
)

@Serializable
data class NetworkMessagesEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkMessagesData
)

@Serializable
data class NetworkMessagesData(
    val messages: NetworkMessagesContent
)

@Serializable
data class NetworkMessagesContent(
    val elements: List<NetworkMessage>,
    val queryParams: NetworkMessageQueryParams? = null
)

@Serializable
data class NetworkMessageQueryParams(
    val before: String? = null
)

@Serializable
data class NetworkMessageEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkMessageData
)

@Serializable
data class NetworkMessageData(
    val message: NetworkMessage
)

@Serializable
data class NetworkMessage(
    val id: String,
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("sender_id") val senderId: String,
    @SerialName("sender_name") val senderName: String,
    @SerialName("sender_avatar") val senderAvatar: String? = null,
    val type: String, // "text" | "image" | "file"
    val content: String? = null,
    @SerialName("media_url") val mediaUrl: String? = null,
    @SerialName("is_deleted") val isDeleted: Boolean = false,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class NetworkCreateDirectChatRequest(
    @SerialName("user_id") val userId: String
)

@Serializable
data class NetworkCreateGroupChatRequest(
    val name: String,
    @SerialName("participant_ids") val participantIds: List<String>
)

@Serializable
data class NetworkSendMessageRequest(
    val content: String? = null,
    val type: String,
    @SerialName("media_url") val mediaUrl: String? = null
)

@Serializable
data class NetworkUnreadEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkUnreadData
)

@Serializable
data class NetworkUnreadData(
    val unread: NetworkUnreadInfo
)

@Serializable
data class NetworkUnreadInfo(
    @SerialName("conversation_id") val conversationId: String,
    val count: Int
)

@Serializable
data class NetworkReadEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkReadData
)

@Serializable
data class NetworkReadData(
    val read: NetworkReadInfo
)

@Serializable
data class NetworkReadInfo(
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("read_at") val readAt: String? = null
)
