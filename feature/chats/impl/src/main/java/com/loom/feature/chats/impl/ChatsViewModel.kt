package com.loom.feature.chats.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.ChatsRepository
import com.loom.core.network.model.NetworkConversation
import com.loom.core.network.model.NetworkMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatsRepository: ChatsRepository,
    private val json: Json,
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<NetworkConversation>>(emptyList())
    val conversations: StateFlow<List<NetworkConversation>> = _conversations.asStateFlow()

    private val _messages = MutableStateFlow<List<NetworkMessage>>(emptyList())
    val messages: StateFlow<List<NetworkMessage>> = _messages.asStateFlow()

    private val _activeConversationId = MutableStateFlow<String?>(null)
    
    init {
        loadConversations()
    }

    fun loadConversations() {
        viewModelScope.launch {
            chatsRepository.getConversations().collect { list ->
                _conversations.value = list
            }
        }
    }

    fun selectConversation(conversationId: String) {
        _activeConversationId.value = conversationId
        _messages.value = emptyList() // Clear messages for new conversation
        chatsRepository.disconnectFromChat()
        chatsRepository.connectToChat(conversationId)
        
        loadMessages(conversationId)
        observeChatEvents()
    }

    private fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            chatsRepository.getMessages(conversationId).collect { list ->
                _messages.value = list
            }
        }
    }

    private fun observeChatEvents() {
        viewModelScope.launch {
            chatsRepository.chatEvents.collectLatest { event ->
                try {
                    val jsonObj = json.parseToJsonElement(event).jsonObject
                    val type = jsonObj["event"]?.jsonPrimitive?.content
                    
                    if (type == "new_message") {
                        val messageJson = jsonObj["message"].toString()
                        val newMessage = json.decodeFromString<NetworkMessage>(messageJson)
                        
                        _messages.update { current ->
                            if (current.none { it.id == newMessage.id }) {
                                current + newMessage
                            } else current
                        }
                    }
                } catch (e: Exception) {
                    // Log error
                }
            }
        }
    }

    fun sendMessage(content: String) {
        val conversationId = _activeConversationId.value ?: return
        viewModelScope.launch {
            try {
                chatsRepository.sendMessage(conversationId, content)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun setTyping(isTyping: Boolean) {
        val conversationId = _activeConversationId.value ?: return
        viewModelScope.launch {
            chatsRepository.setTyping(conversationId, isTyping)
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatsRepository.disconnectFromChat()
    }
}
