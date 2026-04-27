package com.loom.core.model.data

import kotlinx.serialization.Serializable

@Serializable
data class EventProfile(
    val id: Long,
    val creator: UserProfile,
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
    val participants: List<UserProfile>,
    val date: String
)