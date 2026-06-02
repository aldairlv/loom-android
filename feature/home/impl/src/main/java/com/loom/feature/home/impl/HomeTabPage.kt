package com.loom.feature.home.impl

import androidx.compose.runtime.Composable
import com.loom.feature.home.impl.routes.FollowingFeedRoute
import com.loom.feature.home.impl.routes.ForYouRoute
import com.loom.feature.home.impl.routes.TagsRoute

@Composable
fun HomeTabPage(
    tab: HomeTab,
    onCommentClick: (String) -> Unit = {},
    onRepostWithComment: (com.loom.core.model.data.PostFeedItem) -> Unit = {},
) {

    when (tab) {
        HomeTab.ForYou -> ForYouRoute(
            onCommentClick = onCommentClick,
            onCommentRepostClick = onRepostWithComment
        )

        HomeTab.Following -> FollowingFeedRoute(
            onCommentClick = onCommentClick,
            onCommentRepostClick = onRepostWithComment
        )

        HomeTab.Tags -> TagsRoute(
            onCommentClick = onCommentClick,
            onCommentRepostClick = onRepostWithComment
        )

    }
}
