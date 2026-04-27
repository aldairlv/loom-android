package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTitle(
    val id: String,
    val text: String
)
