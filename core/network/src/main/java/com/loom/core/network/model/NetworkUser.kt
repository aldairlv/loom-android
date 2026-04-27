package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUser(
    val id: String,
    val username: String,
    val avatar: String,
    val userViewUrl: String,
    val canBeFollowed: Boolean,
    val canShowBages: Boolean,
    val description: String,
    val followed: Boolean,
    val isAdult: Boolean,
    val title: String,
    val uuid: String,
    val posts: List<NetworkPost> = emptyList()
)
