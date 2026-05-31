package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("carousel")
data class NetworkObjectCarousel(
    override val objectType: String,
    override val id: String,
    override val streamGlobalPosition: Int,
    override val streamSessionId: String? = null,
    val elements: List<NetworkObjectCarouselElement>
) : NetworkObject