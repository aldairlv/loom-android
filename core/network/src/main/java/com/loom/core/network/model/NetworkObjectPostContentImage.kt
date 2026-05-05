package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
data class NetworkContentObjectImage(
    override val type: String,
    val media: List<NetworkMedia>
): NetworkContentObject {
}