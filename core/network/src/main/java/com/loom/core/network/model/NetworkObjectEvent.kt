package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("event")
data class NetworkObjectEvent(
    override val objectType: String,
    override val id: String,
    override val streamGlobalPosition: Int,
    override val streamSessionId: String? = null,
    val timestamp: Long,
    val tags: List<String> = emptyList(),
    val creator: NetworkEventCreator,
    @SerialName("event_data")
    val eventData: NetworkEventData,
    @SerialName("friends_attending")
    val friendsAttending: List<NetworkFriendAttending> = emptyList(),
    val distance: String? = null
) : NetworkObject

@Serializable
data class NetworkFriendAttending(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("avatar_url")
    val avatarUrl: String?
)

@Serializable
data class NetworkEventCreator(
    @SerialName("creator_display_name")
    val displayName: String,
    @SerialName("avatar_url")
    val avatarUrl: String?
)

@Serializable
data class NetworkEventData(
    val title: String,
    val description: String,
    val assets: List<NetworkPostMedia>,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    val location: NetworkEventLocation,
    @SerialName("rsvp_count")
    val rsvpCount: Int,
    @SerialName("max_attendees")
    val maxAttendees: Int?,
    @SerialName("is_online")
    val isOnline: Boolean,
    @SerialName("is_public")
    val isPublic: Boolean,
    @SerialName("is_cancelled")
    val isCancelled: Boolean,
    val status: String,
    val category: String
)

@Serializable
data class NetworkEventLocation(
    @SerialName("location_name")
    val name: String,
    @SerialName("location_address")
    val address: String? = null,
    val coordinates: NetworkEventCoordinates? = null
)

@Serializable
data class NetworkEventCoordinates(
    val latitude: Double,
    val longitude: Double
)
