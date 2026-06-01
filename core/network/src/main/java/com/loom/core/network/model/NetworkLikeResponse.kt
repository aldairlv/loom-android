package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLikeResponse(
    val id: String,
    val profile: String,
    val post: String,
    val created_at: String
)
