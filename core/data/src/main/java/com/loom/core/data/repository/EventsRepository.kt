package com.loom.core.data.repository

interface EventsRepository {
    suspend fun getFeedEventsSoon(
        cursor: String? = null,
        isRefresh: Boolean = false
    ): FeedObjectsResult
}
