package com.loom.core.ui.profile

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.loom.core.model.data.FollowingUserFeedItem
import com.loom.core.model.data.PostFeedItem
import com.loom.core.ui.post.PostFeedCard


fun LazyListScope.feedFollowingUsers(
    followingUsers: List<FollowingUserFeedItem>,
    onUnfollow: (String) -> Unit,
    onBlock: (String) -> Unit,
) {
    items(
        items = followingUsers,
        key = { it.id }
    ) { followingUser ->
        FollowingUserFeedCard(
            followingUserFeed = followingUser,
            onUnfollow = { onUnfollow(followingUser.id) },
            onBlock = { onBlock(followingUser.id) },

        )
    }
}
