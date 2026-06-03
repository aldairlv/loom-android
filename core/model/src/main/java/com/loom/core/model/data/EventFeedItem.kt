package com.loom.core.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EventFeedItem(
    val id: String,
    val timestamp: Long,
    val tags: List<String> = emptyList(),
    val creator: EventCreator,
    val eventData: EventData,
    val friendsAttending: List<FriendAttending> = emptyList(),
    val distance: Double? = null
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
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val assets: List<PostMedia> = emptyList(),
    val startTime: Instant,
    val endTime: Instant? = null,
    val timezone: String? = null,
    val location: EventLocation? = null,
    val rsvpCount: Int = 0,
    val maxAttendees: Int? = null,
    val isOnline: Boolean = false,
    val isPublic: Boolean = true,
    val isCancelled: Boolean = false,
    val status: String? = null,
    val category: String? = null
)

@Serializable
data class EventLocation(
    val name: String,
    val address: String? = null,
    val coordinates: EventCoordinates? = null
)

@Serializable
data class EventCoordinates(
    val latitude: Double,
    val longitude: Double
)
