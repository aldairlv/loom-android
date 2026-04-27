package com.loom.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.loom.core.database.model.PopulatedTimelineObject
import com.loom.core.database.model.TimelineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimelineDao {
    @Transaction
    @Query("""
        SELECT * FROM timeline_objects 
        WHERE timeline_category = :category
        ORDER BY stream_global_position ASC
    """)
    fun getTimeline(category: String): Flow<List<PopulatedTimelineObject>>

    @Upsert
    suspend fun upsertTimelineObjects(entities: List<TimelineEntity>)

    @Query("""
        DELETE FROM timeline_objects 
        WHERE timeline_category = :category 
    """)
    suspend fun clearTimelineObjects(category: String)
}