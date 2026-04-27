package com.loom.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class NetworkMedia(
    val url: String,
    val type: String,
    val width: Int,
    val height: Int
)