package com.loom.core.network.websocket

import android.util.Log
import com.loom.core.network.BuildConfig
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class ChatWebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val json: Json,
) {
    private var webSocket: WebSocket? = null

    private val _events = MutableSharedFlow<String>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<String> = _events.asSharedFlow()

    fun connect(conversationId: String, token: String) {
        if (webSocket != null) return

        val baseUrl = BuildConfig.BACKEND_URL
            .replace("http://", "ws://")
            .replace("https://", "wss://")
        
        val wsUrl = "$baseUrl/ws/chat/$conversationId/?token=$token"

        val request = Request.Builder()
            .url(wsUrl)
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("ChatWebSocket", "WebSocket connected for $conversationId")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("ChatWebSocket", "Message received: $text")
                _events.tryEmit(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                this@ChatWebSocketClient.webSocket = null
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("ChatWebSocket", "WebSocket failure: ${t.message}", t)
                this@ChatWebSocketClient.webSocket = null
            }
        })
    }

    fun sendMessage(content: String, type: String = "text", mediaUrl: String? = null) {
        val action = WebSocketAction(
            action = "send_message",
            content = content,
            type = type,
            media_url = mediaUrl
        )
        sendAction(action)
    }

    fun markRead() {
        sendAction(WebSocketAction(action = "mark_read"))
    }

    fun setTyping(isTyping: Boolean) {
        sendAction(WebSocketAction(action = "typing", is_typing = isTyping))
    }

    fun ping() {
        sendAction(WebSocketAction(action = "ping"))
    }

    private fun sendAction(action: WebSocketAction) {
        val jsonString = json.encodeToString(action)
        webSocket?.send(jsonString)
    }

    fun disconnect() {
        webSocket?.close(1000, "Normal closure")
        webSocket = null
    }

    @Serializable
    private data class WebSocketAction(
        val action: String,
        val content: String? = null,
        val type: String? = null,
        val media_url: String? = null,
        val is_typing: Boolean? = null
    )
}
