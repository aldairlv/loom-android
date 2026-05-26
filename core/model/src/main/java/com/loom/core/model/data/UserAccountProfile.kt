package com.loom.core.model.data

data class UserAccountProfile(
    val id: String,
    val user: String,
    val displayName: String,
    val bio: String?,
    val city: String?,
    val timezone: String?,
    val canBeFollowed: Boolean?,
    val avatarUrl: String?,
    val bannerUrl: String?,
    val locationCoords: LocationCoords?
)

data class LocationCoords(
    val latitude: Double,
    val longitude: Double
)
