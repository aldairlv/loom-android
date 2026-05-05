package com.loom.core.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.loom.core.database.model.TagEntity
import com.loom.core.database.model.TrendEntity

@Dao
interface TagDao {

    @Upsert
    suspend fun upsertTags(tags: List<TagEntity>)

    @Upsert
    suspend fun upsertTag(tag: TagEntity)

}