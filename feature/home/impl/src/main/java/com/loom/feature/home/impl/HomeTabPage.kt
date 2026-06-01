package com.loom.feature.home.impl

import androidx.compose.runtime.Composable
import com.loom.feature.home.impl.routes.FollowingFeedRoute
import com.loom.feature.home.impl.routes.ForYouRoute
import com.loom.feature.home.impl.routes.TagsRoute

@Composable
fun HomeTabPage(
    tab: HomeTab,
    onRepostWithComment: (com.loom.core.model.data.PostFeedItem) -> Unit = {},
) {

    when (tab) {
        HomeTab.ForYou -> ForYouRoute(onCommentRepostClick = onRepostWithComment)

        HomeTab.Following -> FollowingFeedRoute(onCommentRepostClick = onRepostWithComment)

        HomeTab.Tags -> TagsRoute(onCommentRepostClick = onRepostWithComment)

    }
}