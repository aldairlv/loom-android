package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("video")
data class NetworkObjectTrendElementVideo(
    override val id: String,
    override val objectType: String,
    val resource: List<NetworkPost>
): NetworkObjectTrendElement