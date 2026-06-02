package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkFollowResponse(
    val id: String,
    val from_profile: NetworkFollowProfile,
    val to_profile: NetworkFollowProfile,
    val created_at: String
)

@Serializable
data class NetworkFollowProfile(
    val id: String,
    val username: String,
    val display_name: String,
    val avatar: String? = null,
    val last_posted_at: String? = null
)
