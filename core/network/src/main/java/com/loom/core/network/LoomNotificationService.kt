package com.loom.core.network

import kotlinx.coroutines.flow.Flow

interface LoomNotificationService {
    val messages: Flow<String>
    fun connect(authToken: String)
    fun disconnect()
}
