package com.loom.core.network

import com.loom.core.network.model.NetworkPost

interface LoomNetworkDataSource {
    suspend fun getPosts(): List<NetworkPost>
}