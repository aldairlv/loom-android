package com.loom.core.model.data

import kotlinx.serialization.Serializable

@Serializable
data class TagProfile(
    val name: String,
    val isFollowed: Boolean,
)