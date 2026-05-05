package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("carousel")
data class NetworkObjectCarousel(
    override val objectType: String,
    override val id: String,
    override val streamGlobalPosition: Int,
    val elements: List<NetworkObjectCarouselElement>
) : NetworkObject