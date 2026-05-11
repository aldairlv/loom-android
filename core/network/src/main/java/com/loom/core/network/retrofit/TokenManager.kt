package com.loom.core.network.retrofit

import kotlinx.coroutines.flow.Flow


interface TokenManager {
    val accessToken: Flow<String?>
    suspend fun getRefreshToken(): String?
    suspend fun updateTokens(access: String, refresh: String?)
    suspend fun clearTokens()
}
