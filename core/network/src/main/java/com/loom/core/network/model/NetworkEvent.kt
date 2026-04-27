package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkEvent(
    val id: Long,
    val creator: NetworkUser,
    val poster: String,
    val eventViewUrl: String,
    val canBeJoined: Boolean,
    val description: String,
    val joined: Boolean,
    val isAdultOnly: Boolean,
    val title: String,
    val uuid: String,
    val tags: List<String>,
    val numberParticipants: Int,
    val participants: List<NetworkUser>,
    val date: String
)
