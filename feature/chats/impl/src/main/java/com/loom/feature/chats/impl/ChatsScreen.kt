package com.loom.feature.chats.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.loom.core.network.model.NetworkConversation
import com.loom.core.network.model.NetworkMessage

@Composable
fun ChatsRoute(
    onConversationClick: (String) -> Unit,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsState()

    ChatsScreen(
        conversations = conversations,
        onConversationClick = { id ->
            viewModel.selectConversation(id)
            onConversationClick(id)
        }
    )
}

@Composable
fun ChatsScreen(
    conversations: List<NetworkConversation>,
    onConversationClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(conversations) { conversation ->
            ConversationItem(
                conversation = conversation,
                onClick = { onConversationClick(conversation.id) }
            )
        }
    }
}

@Composable
fun ConversationItem(
    conversation: NetworkConversation,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(conversation.displayName) },
        supportingContent = { Text(conversation.lastMessage?.content ?: "") },
        trailingContent = {
            if (conversation.unreadCount > 0) {
                Badge { Text(conversation.unreadCount.toString()) }
            }
        }
    )
}

@Composable
fun ChatConversationRoute(
    conversationId: String,
    onBackClick: () -> Unit,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()

    ChatConversationScreen(
        messages = messages,
        onBackClick = onBackClick,
        onSendMessage = viewModel::sendMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatConversationScreen(
    messages: List<NetworkMessage>,
    onBackClick: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
                navigationIcon = {
                    TextButton(onClick = onBackClick) { Text("Back") }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    if (text.isNotBlank()) {
                        onSendMessage(text)
                        text = ""
                    }
                }) {
                    Text("Send")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding,
            reverseLayout = false // Backend sends them oldest to newest
        ) {
            items(messages) { message ->
                MessageItem(message = message)
            }
        }
    }
}

@Composable
fun MessageItem(message: NetworkMessage) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = message.senderName, style = MaterialTheme.typography.labelSmall)
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(
                text = message.content ?: "",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
