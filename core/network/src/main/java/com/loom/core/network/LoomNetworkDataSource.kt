package com.loom.core.network

import com.loom.core.network.model.NetworkAuthResponse
import com.loom.core.network.model.NetworkLoginRequest
import com.loom.core.network.model.NetworkLogoutResponse
import com.loom.core.network.model.NetworkObjectsResponse
import com.loom.core.network.model.NetworkPost
import com.loom.core.network.model.NetworkRegisterRequest
import com.loom.core.network.model.NetworkTimelineResponse
import com.loom.core.network.model.NetworkTokenResponse
import com.loom.core.network.model.NetworkValidateEmailResponse

interface LoomNetworkDataSource {
    suspend fun login(request: NetworkLoginRequest): NetworkAuthResponse

    suspend fun register(request: NetworkRegisterRequest): NetworkAuthResponse

    suspend fun logout(): NetworkLogoutResponse

    suspend fun refreshToken(refreshToken: String): NetworkTokenResponse

    suspend fun getPosts(): List<NetworkPost>

    suspend fun getExplore(
        cursor: String? = null
    ): NetworkObjectsResponse

    suspend fun getTimeline(
        timelineCategory: String,
        cursor: String? = null
    ): NetworkObjectsResponse

    suspend fun validateEmail(
        email: String
    ): NetworkValidateEmailResponse
}