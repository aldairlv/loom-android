package com.loom.core.network

import com.loom.core.network.model.NetworkPost
import com.loom.core.network.model.NetworkTimelineResponse

interface LoomNetworkDataSource {
    suspend fun getPosts(): List<NetworkPost>

    suspend fun getTimeline(
        timelineCategory: String,
        cursor: String? = null
    ): NetworkTimelineResponse
}