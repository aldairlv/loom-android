package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkObjectsResponse(
    val elements: List<NetworkObject>,
    val cursor: String? = null
)