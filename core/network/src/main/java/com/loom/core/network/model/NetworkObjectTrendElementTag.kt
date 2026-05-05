package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
@SerialName("tag")
data class NetworkObjectTrendElementTag(
    override val id: String,
    override val objectType: String,
    val name: String,
    val isFollowed: Boolean,
    val resource: List<NetworkPost>
): NetworkObjectTrendElement