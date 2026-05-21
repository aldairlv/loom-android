package com.loom.core.data.repository

import com.loom.core.model.data.PostsFeedResult

interface HomeRepository {
    suspend fun getPostsFeedForYou(cursor: String? = null): PostsFeedResult
    suspend fun getPostsFeedFollowing(cursor: String? = null): PostsFeedResult
    suspend fun getPostsFeedTags(cursor: String? = null): PostsFeedResult
}
