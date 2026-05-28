package com.loom.core.data.repository

import com.loom.core.common.result.Result
import com.loom.core.model.data.FollowingUsersResult
import com.loom.core.model.data.PostsFeedResult
import com.loom.core.model.data.UserAccountProfile

interface ProfileRepository {

    suspend fun getMyProfile(): Result<UserAccountProfile>

    suspend fun updateProfile(
        id: String,
        displayName: String? = null,
        bio: String? = null,
        city: String? = null,
        timezone: String? = null,
        canBeFollowed: Boolean? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        avatar: ByteArray? = null,
        banner: ByteArray? = null
    ): Result<UserAccountProfile>

    suspend fun getMyPosts(cursor: String? = null): Result<PostsFeedResult>

    suspend fun getLikedPosts(cursor: String? = null): Result<PostsFeedResult>

    suspend fun getFollowingUsers(cursor: String? = null): Result<FollowingUsersResult>
}