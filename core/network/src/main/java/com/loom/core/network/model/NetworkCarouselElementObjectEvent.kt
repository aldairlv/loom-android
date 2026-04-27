package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("event_card")
class NetworkCarouselElementObjectEvent (
    override val id: String,
    val tags: List<String>,
    val resource: List<NetworkEvent>,
): NetworkCarouselElementObject {
}