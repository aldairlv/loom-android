package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTimelineResponse(
    val response: NetworkTimelineWrapper
)

@Serializable
data class NetworkTimelineWrapper(
    val timeline: NetworkObjectsResponse
)

