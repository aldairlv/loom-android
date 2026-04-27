package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("carousel")
data class NetworkTimelineObjectCarousel(
    override val objectType: String,
    override val id: String,
    override val streamGlobalPosition: Int,
    val elements: List<NetworkCarouselElementObject>
) : NetworkTimelineObject