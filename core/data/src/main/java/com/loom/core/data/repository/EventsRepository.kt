package com.loom.core.data.repository

import com.loom.core.model.data.EventFeedItem

interface EventsRepository {
    suspend fun getFeedEventsSoon(
        cursor: String? = null,
        isRefresh: Boolean = false
    ): FeedObjectsResult

    suspend fun getEvent(id: String): EventFeedItem?

    suspend fun createEvent(
        title: String,
        description: String? = null,
        startTime: String,
        endTime: String? = null,
        locationName: String? = null,
        locationAddress: String? = null,
        assetIds: List<String> = emptyList(),
        thumbnailId: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        category: String? = null,
        maxAttendees: Int? = null,
        isOnline: Boolean? = null,
        isPublic: Boolean? = null,
        status: String? = null,
        tags: List<String> = emptyList(),
        timezone: String? = null,
    ): EventFeedItem?
}
