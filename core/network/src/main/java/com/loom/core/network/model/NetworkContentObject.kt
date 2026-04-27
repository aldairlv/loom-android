package com.loom.core.network.model

import com.loom.core.network.serialization.polymorphic.NetworkContentObjectSerializer
import kotlinx.serialization.Serializable

@Serializable(with = NetworkContentObjectSerializer::class)

sealed interface NetworkContentObject {
    val type: String
}

