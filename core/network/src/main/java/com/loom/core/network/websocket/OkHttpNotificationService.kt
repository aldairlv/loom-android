package com.loom.core.network.websocket

import android.util.Log
import com.loom.core.network.BuildConfig
import com.loom.core.network.LoomNotificationService
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OkHttpNotificationService @Inject constructor(
    private val okHttpClient: OkHttpClient,
) : LoomNotificationService {

    private var webSocket: WebSocket? = null
    
    private val _messages = MutableSharedFlow<String>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val messages: SharedFlow<String> = _messages.asSharedFlow()

    override fun connect(authToken: String) {
        if (webSocket != null) return

        val baseUrl = BuildConfig.BACKEND_URL
            .replace("http://", "ws://")
            .replace("https://", "wss://")
        
        val wsUrl = "$baseUrl/ws/notifications/?token=$authToken"

        val request = Request.Builder()
            .url(wsUrl)
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("LoomWebSocket", "Connected to notifications")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("LoomWebSocket", "Message received: $text")
                _messages.tryEmit(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                Log.d("LoomWebSocket", "Closing: $code / $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                this@OkHttpNotificationService.webSocket = null
                Log.d("LoomWebSocket", "Closed: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("LoomWebSocket", "Error: ${t.message}", t)
                this@OkHttpNotificationService.webSocket = null
                // Optional: Implement reconnection logic here or in a higher layer
            }
        })
    }

    override fun disconnect() {
        webSocket?.close(1000, "User logged out or app backgrounded")
        webSocket = null
    }
}
