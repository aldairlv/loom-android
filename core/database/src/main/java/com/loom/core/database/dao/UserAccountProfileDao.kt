package com.loom.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.loom.core.database.model.UserAccountProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAccountProfileDao {
    @Query("SELECT * FROM user_account_profiles WHERE id = :id")
    fun getUserAccountProfile(id: String): Flow<UserAccountProfileEntity?>

    @Query("SELECT * FROM user_account_profiles WHERE user = :userId")
    fun getUserAccountProfileByUserId(userId: String): Flow<UserAccountProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserAccountProfile(profile: UserAccountProfileEntity)

    @Query("DELETE FROM user_account_profiles WHERE id = :id")
    suspend fun deleteUserAccountProfile(id: String)
}
