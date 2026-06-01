package com.loom.feature.profile.impl

import androidx.compose.runtime.Composable
import com.loom.feature.profile.impl.routes.FollowingUsersRoute
import com.loom.feature.profile.impl.routes.LikesRoute
import com.loom.feature.profile.impl.routes.PostsRoute

@Composable
fun ProfileTabPage(
    tab: ProfileTab,
    onRepostWithComment: (com.loom.core.model.data.PostFeedItem) -> Unit = {},
) {
    when (tab) {
        ProfileTab.Posts -> PostsRoute(onCommentRepostClick = onRepostWithComment)
        ProfileTab.Likes -> LikesRoute(onCommentRepostClick = onRepostWithComment)
        ProfileTab.FollowingUsers -> FollowingUsersRoute()
    }
}
