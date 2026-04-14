package com.loom.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPost(
    val id: String,
    val author: String,
    val content: String,
    val imageUrl: String? = null,
    val publishDate: Instant,
)