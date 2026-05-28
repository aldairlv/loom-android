package com.loom.core.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class FollowingUserFeedItem(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String?,
    val last_posted_at: Instant?
)

data class FollowingUsersResult(
    val users: List<FollowingUserFeedItem>,
    val nextCursor: String?
)
