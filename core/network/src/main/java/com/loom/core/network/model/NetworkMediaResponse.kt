package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkMediaResponse(
    val id: String,
    val url: String,
    val type: String,
    val width: Int,
    val height: Int
)
