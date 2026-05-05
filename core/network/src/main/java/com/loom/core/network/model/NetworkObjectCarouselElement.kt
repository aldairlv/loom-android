package com.loom.core.network.model

import com.loom.core.network.serialization.polymorphic.NetworkCarouselElementObjectSerializer
import kotlinx.serialization.Serializable

@Serializable(with = NetworkCarouselElementObjectSerializer::class)
sealed interface NetworkObjectCarouselElement {
    val id: String
}
