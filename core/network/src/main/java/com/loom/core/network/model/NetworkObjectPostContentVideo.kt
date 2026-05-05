package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("video")
data class NetworkContentObjectVideo(
    override val type: String,
    val media: List<NetworkMedia>
): NetworkContentObject {
}