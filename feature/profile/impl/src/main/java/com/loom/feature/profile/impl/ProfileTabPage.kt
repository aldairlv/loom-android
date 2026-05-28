package com.loom.feature.profile.impl

import androidx.compose.runtime.Composable
import com.loom.feature.profile.impl.routes.FollowingUsersRoute
import com.loom.feature.profile.impl.routes.LikesRoute
import com.loom.feature.profile.impl.routes.PostsRoute

@Composable
fun ProfileTabPage(
    tab: ProfileTab,
) {
    when (tab) {
        ProfileTab.Posts -> PostsRoute()
        ProfileTab.Likes -> LikesRoute()
        ProfileTab.FollowingUsers -> FollowingUsersRoute()
    }
}
