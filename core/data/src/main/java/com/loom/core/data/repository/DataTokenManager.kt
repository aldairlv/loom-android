package com.loom.core.data.repository

import com.loom.core.datastore.LoomPreferencesDataSource
import com.loom.core.network.retrofit.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal class DataTokenManager @Inject constructor(
    private val loomPreferencesDataSource: LoomPreferencesDataSource
) : TokenManager {
    override val accessToken: Flow<String?> = loomPreferencesDataSource.userData.map { it.accessToken }

    override suspend fun getRefreshToken(): String? = loomPreferencesDataSource.userData.first().refreshToken

    override suspend fun updateTokens(access: String, refresh: String?) {
        val currentData = loomPreferencesDataSource.userData.first()
        loomPreferencesDataSource.setTokens(
            accessToken = access,
            refreshToken = refresh ?: currentData.refreshToken,
            userId = currentData.userId
        )
    }

    override suspend fun clearTokens() {
        loomPreferencesDataSource.clearTokens()
    }
}
