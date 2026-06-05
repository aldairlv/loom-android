package com.loom.core.data.repository

import com.loom.core.model.data.UserAccountProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserAccountProfile(id: String): Flow<UserAccountProfile?>
    fun getUserAccountProfileByUserId(userId: String): Flow<UserAccountProfile?>
    suspend fun syncUserAccountProfile(id: String)
    suspend fun syncMyProfile()
    suspend fun registerDevice(deviceId: String, fcmToken: String? = null, isActive: Boolean = true)
}
