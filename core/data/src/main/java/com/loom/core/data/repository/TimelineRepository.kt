package com.loom.core.data.repository

import com.loom.core.data.Syncable
import com.loom.core.model.data.TimelineObject
import com.loom.core.model.enum.TimelineCategory
import kotlinx.coroutines.flow.Flow

interface TimelineRepository : Syncable {
    fun getTimelineObjects(timelineCategory: TimelineCategory): Flow<List<TimelineObject>>
    suspend fun syncTimeline(timelineCategory: TimelineCategory, forceRefresh: Boolean): Boolean
}