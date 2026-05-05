package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkExploreResponse(
    val response: NetworkExploreWrapper
)

@Serializable
data class NetworkExploreWrapper(
    val explore: NetworkObjectsResponse
)

