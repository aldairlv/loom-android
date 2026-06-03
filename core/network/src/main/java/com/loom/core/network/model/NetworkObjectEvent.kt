package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("event")
data class NetworkObjectEvent(
    override val objectType: String = "event",
    override val id: String,
    override val streamGlobalPosition: Int? = null,
    override val streamSessionId: String? = null,
    val timestamp: Long,
    val tags: List<String> = emptyList(),
    val creator: NetworkEventCreator,
    @SerialName("event_data")
    val eventData: NetworkEventData,
    @SerialName("friends_attending")
    val friendsAttending: List<NetworkFriendAttending> = emptyList(),
    val distance: Double? = null,
    @SerialName("user_rsvp_status") val userRsvpStatus: String? = null,
    @SerialName("access_details") val accessDetails: NetworkEventAccessDetails? = null,
    @SerialName("attendees_sample") val attendeesSample: List<NetworkEventCreator> = emptyList()
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
    val id: String? = null,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    val bio: String? = null,
    @SerialName("is_followed") val isFollowed: Boolean = false
)

@Serializable
data class NetworkEventData(
    val title: String,
    val description: String? = null,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,
    val assets: List<NetworkPostMedia> = emptyList(),
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String? = null,
    val timezone: String? = null,
    val location: NetworkEventLocation? = null,
    @SerialName("rsvp_count")
    val rsvpCount: Int = 0,
    @SerialName("max_attendees")
    val maxAttendees: Int? = null,
    @SerialName("is_online")
    val isOnline: Boolean = false,
    @SerialName("is_public")
    val isPublic: Boolean = true,
    @SerialName("is_cancelled")
    val isCancelled: Boolean = false,
    val status: String? = null,
    val category: String? = null,
    val requirements: List<String> = emptyList(),
    @SerialName("requires_qr_checkin") val requiresQrCheckin: Boolean = false,
    @SerialName("payment_details") val paymentDetails: NetworkEventPaymentDetails? = null,
    @SerialName("contact_channels") val contactChannels: NetworkEventContactChannels? = null,
    @SerialName("rating_summary") val ratingSummary: NetworkEventRatingSummary? = null
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

@Serializable
data class NetworkEventAccessDetails(
    @SerialName("meeting_instructions") val meetingInstructions: String? = null,
    @SerialName("live_stream_url") val liveStreamUrl: String? = null,
    @SerialName("secure_attendance_token") val secureAttendanceToken: String? = null
)

@Serializable
data class NetworkEventPaymentDetails(
    @SerialName("requires_payment") val requiresPayment: Boolean = false,
    val price: Double? = null,
    val currency: String? = null,
    @SerialName("payment_methods_allowed") val paymentMethodsAllowed: List<String> = emptyList(),
    @SerialName("stripe_price_id") val stripePriceId: String? = null
)

@Serializable
data class NetworkEventContactChannels(
    @SerialName("support_email") val supportEmail: String? = null,
    @SerialName("support_url") val supportUrl: String? = null,
    @SerialName("website_url") val websiteUrl: String? = null,
    @SerialName("whatsapp_url") val whatsappUrl: String? = null,
    @SerialName("telegram_url") val telegramUrl: String? = null,
    @SerialName("discord_url") val discordUrl: String? = null,
    @SerialName("zoom_url") val zoomUrl: String? = null
)

@Serializable
data class NetworkEventRatingSummary(
    @SerialName("average_rating") val averageRating: Double? = null,
    @SerialName("total_reviews") val totalReviews: Int = 0
)
