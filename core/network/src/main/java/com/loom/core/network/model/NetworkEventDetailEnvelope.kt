package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkEventDetailEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkObject
)
