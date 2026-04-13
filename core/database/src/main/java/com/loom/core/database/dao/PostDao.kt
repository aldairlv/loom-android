package com.loom.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.loom.core.database.model.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY publish_date DESC")
    fun getPostEntities(): Flow<List<PostEntity>>

    @Upsert
    suspend fun upsertPosts(entities: List<PostEntity>)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePost(id: String)
}