package com.loom.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.loom.core.database.model.EventEntity
import com.loom.core.database.model.PopulatedEventProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Transaction
    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventProfile(eventId: Long): Flow<PopulatedEventProfile>

    @Upsert
    suspend fun upsertEvents(events: List<EventEntity>)
}
