package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
data class NetworkContentObjectText (
    override val type: String,
    val text: String
): NetworkContentObject
