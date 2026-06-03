package com.loom.core.network.model

import com.loom.core.network.serialization.polymorphic.NetworkObjectSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable(with = NetworkObjectSerializer::class)
@Serializable
@SerialName("trend")
class NetworkObjectTrend (
    override val objectType: String = "trend",
    override val id: String,
    override val streamGlobalPosition: Int? = null,
    override val streamSessionId: String? = null,
    val category: String,
    val count: String,
    val iconUrl: String,
    val subType: String,
    val elements: List<NetworkObjectTrendElement>,
): NetworkObject {
}