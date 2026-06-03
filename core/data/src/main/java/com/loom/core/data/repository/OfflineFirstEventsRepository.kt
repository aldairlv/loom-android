package com.loom.core.data.repository

import com.loom.core.model.data.EventCoordinates
import com.loom.core.model.data.EventCreator
import com.loom.core.model.data.EventData
import com.loom.core.model.data.EventFeedItem
import com.loom.core.model.data.EventLocation
import com.loom.core.model.data.FeedObject
import com.loom.core.model.data.FriendAttending
import com.loom.core.model.data.PostMedia
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkEventCoordinates
import com.loom.core.network.model.NetworkEventCreator
import com.loom.core.network.model.NetworkEventData
import com.loom.core.network.model.NetworkEventLocation
import com.loom.core.network.model.NetworkFriendAttending
import com.loom.core.network.model.NetworkObject
import com.loom.core.network.model.NetworkObjectEvent
import com.loom.core.network.model.NetworkPostMedia
import kotlinx.datetime.Instant
import javax.inject.Inject

internal class OfflineFirstEventsRepository @Inject constructor(
    private val network: LoomNetworkDataSource,
) : EventsRepository {

    override suspend fun getFeedEventsSoon(cursor: String?, isRefresh: Boolean): FeedObjectsResult {
        val networkResponse = network.getFeedEventsSoon(cursor)
        val feedObjects = networkResponse.results.mapNotNull { it.asExternalModel() }

        return FeedObjectsResult(
            objects = feedObjects,
            nextCursor = networkResponse.next
        )
    }

    override suspend fun getEvent(id: String): EventFeedItem? {
        val networkResponse = network.getEvent(id)
        val eventObject = networkResponse.results.firstOrNull() as? NetworkObjectEvent
        return if (eventObject != null) {
            val feedObject = eventObject.asExternalModel() as? FeedObject.EventFeedObject
            feedObject?.event
        } else null
    }
}

private fun NetworkObject.asExternalModel(): FeedObject? {
    return when (this) {
        is NetworkObjectEvent -> FeedObject.EventFeedObject(
            event = EventFeedItem(
                id = id,
                timestamp = timestamp,
                tags = tags,
                creator = creator.asExternalModel(),
                eventData = eventData.asExternalModel(),
                friendsAttending = friendsAttending.map { it.asExternalModel() },
                distance = distance
            ),
            streamGlobalPosition = streamGlobalPosition ?: 0,
            streamSessionId = streamSessionId
        )
        else -> null
    }
}

private fun NetworkEventCreator.asExternalModel() = EventCreator(
    displayName = displayName,
    avatarUrl = avatarUrl
)

private fun NetworkFriendAttending.asExternalModel() = FriendAttending(
    id = id,
    displayName = displayName,
    avatarUrl = avatarUrl
)

private fun NetworkEventData.asExternalModel() = EventData(
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    assets = assets.map { it.asExternalModel() },
    startTime = Instant.parse(startTime),
    endTime = endTime?.let { Instant.parse(it) },
    timezone = timezone,
    location = location?.asExternalModel(),
    rsvpCount = rsvpCount,
    maxAttendees = maxAttendees,
    isOnline = isOnline,
    isPublic = isPublic,
    isCancelled = isCancelled,
    status = status,
    category = category
)

private fun NetworkPostMedia.asExternalModel() = PostMedia(
    id = id,
    url = url,
    type = type,
    width = width ?: 0,
    height = height ?: 0
)

private fun NetworkEventLocation.asExternalModel() = EventLocation(
    name = name,
    address = address,
    coordinates = coordinates?.asExternalModel()
)

private fun NetworkEventCoordinates.asExternalModel() = EventCoordinates(
    latitude = latitude,
    longitude = longitude
)
