package com.loom.core.network.model

import com.loom.core.network.serialization.polymorphic.NetworkCarouselElementObjectSerializer
import com.loom.core.network.serialization.polymorphic.NetworkTrendElementObjectSerializer
import kotlinx.serialization.Serializable


@Serializable(with = NetworkTrendElementObjectSerializer::class)
sealed interface NetworkObjectTrendElement {
    val id: String
    val objectType: String
}