package com.loom.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.loom.core.database.model.PopulatedUserProfile
import com.loom.core.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserProfile(userId: String): Flow<PopulatedUserProfile>

    @Upsert
    suspend fun upsertUsers(users: List<UserEntity>)
}
