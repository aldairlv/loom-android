package com.loom.core.data.repository

import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkConversation
import com.loom.core.network.model.NetworkMessage
import com.loom.core.network.websocket.ChatWebSocketClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFirstChatsRepository @Inject constructor(
    private val networkDataSource: LoomNetworkDataSource,
    private val chatWebSocketClient: ChatWebSocketClient,
    private val userDataRepository: UserDataRepository,
) : ChatsRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun getConversations(): Flow<List<NetworkConversation>> = flow {
        emit(networkDataSource.getConversations())
    }

    override fun getMessages(conversationId: String, before: String?): Flow<List<NetworkMessage>> = flow {
        emit(networkDataSource.getMessages(conversationId, before))
    }

    override suspend fun createDirectChat(userId: String): NetworkConversation {
        return networkDataSource.createDirectChat(userId)
    }

    override suspend fun createGroupChat(name: String, participantIds: List<String>): NetworkConversation {
        return networkDataSource.createGroupChat(name, participantIds)
    }

    override suspend fun sendMessage(
        conversationId: String,
        content: String,
        type: String,
        mediaUrl: String?
    ) {
        networkDataSource.sendMessage(conversationId, content, type, mediaUrl)
    }

    override suspend fun markAsRead(conversationId: String) {
        networkDataSource.markRead(conversationId)
        chatWebSocketClient.markRead()
    }

    override suspend fun setTyping(conversationId: String, isTyping: Boolean) {
        chatWebSocketClient.setTyping(isTyping)
    }

    override fun connectToChat(conversationId: String) {
        repositoryScope.launch {
            val token = userDataRepository.userData.first().accessToken
            if (!token.isNullOrEmpty()) {
                chatWebSocketClient.connect(conversationId, token)
            }
        }
    }

    override fun disconnectFromChat() {
        chatWebSocketClient.disconnect()
    }

    override val chatEvents: Flow<String> = chatWebSocketClient.events
}
