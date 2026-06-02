package com.loom.core.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EventFeedItem(
    val id: String,
    val timestamp: Long,
    val tags: List<String>,
    val creator: EventCreator,
    val eventData: EventData,
    val friendsAttending: List<FriendAttending> = emptyList(),
    val distance: String? = null
)

@Serializable
data class FriendAttending(
    val id: String,
    val displayName: String,
    val avatarUrl: String?
)

@Serializable
data class EventCreator(
    val displayName: String,
    val avatarUrl: String?
)

@Serializable
data class EventData(
    val title: String,
    val description: String,
    val assets: List<PostMedia>,
    val startTime: Instant,
    val endTime: Instant,
    val location: EventLocation,
    val rsvpCount: Int,
    val maxAttendees: Int?,
    val isOnline: Boolean,
    val isPublic: Boolean,
    val isCancelled: Boolean,
    val status: String,
    val category: String
)

@Serializable
data class EventLocation(
    val name: String,
    val address: String?,
    val coordinates: EventCoordinates?
)

@Serializable
data class EventCoordinates(
    val latitude: Double,
    val longitude: Double
)
