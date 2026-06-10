package com.loom.core.data.repository

import com.loom.core.network.model.NetworkConversation
import com.loom.core.network.model.NetworkMessage
import kotlinx.coroutines.flow.Flow

interface ChatsRepository {
    fun getConversations(): Flow<List<NetworkConversation>>
    fun getMessages(conversationId: String, before: String? = null): Flow<List<NetworkMessage>>
    
    suspend fun createDirectChat(userId: String): NetworkConversation
    suspend fun createGroupChat(name: String, participantIds: List<String>): NetworkConversation
    
    suspend fun sendMessage(conversationId: String, content: String, type: String = "text", mediaUrl: String? = null)
    suspend fun markAsRead(conversationId: String)
    suspend fun setTyping(conversationId: String, isTyping: Boolean)
    
    fun connectToChat(conversationId: String)
    fun disconnectFromChat()
    
    val chatEvents: Flow<String>
}
