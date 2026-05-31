package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkFeedObjectEnvelope(
    val meta: NetworkMeta? = null,
    val response: NetworkFeedObjectData
)

@Serializable
data class NetworkFeedObjectData(
    val feed: NetworkFeedObjectContent
)

@Serializable
data class NetworkFeedObjectContent(
    val elements: List<NetworkObject>,
    @SerialName("queryParams") val queryParams: NetworkQueryParams? = null
)

@Serializable
data class NetworkFeedObjectResponse(
    val next: String?,
    val results: List<NetworkObject>
)
