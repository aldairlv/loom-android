package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkUserProfile(
    val id: String,
    val user: String,
    val username: String,
    val display_name: String,
    val bio: String? = null,
    val city: String? = null,
    val timezone: String? = null,
    val can_be_followed: Boolean? = null,
    val avatar_url: String? = null,
    val banner_url: String? = null,
    val location_coords: NetworkLocationCoords? = null
)

@Serializable
data class NetworkLocationCoords(
    val latitude: Double,
    val longitude: Double
)
