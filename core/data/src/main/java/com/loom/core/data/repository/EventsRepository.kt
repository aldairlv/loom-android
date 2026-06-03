package com.loom.core.data.repository

import com.loom.core.model.data.EventFeedItem

interface EventsRepository {
    suspend fun getFeedEventsSoon(
        cursor: String? = null,
        isRefresh: Boolean = false
    ): FeedObjectsResult

    suspend fun getEvent(id: String): EventFeedItem?
}
