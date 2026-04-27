package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkLoomResponse(
    val response: NetworkTimelineWrapper
)

@Serializable
data class NetworkTimelineWrapper(
    val timeline: NetworkTimelineResponse
)

@Serializable
data class NetworkTimelineResponse(
    val elements: List<NetworkTimelineObject>,
    val cursor: String? = null
)