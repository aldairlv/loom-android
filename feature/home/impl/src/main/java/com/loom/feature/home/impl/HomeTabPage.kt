package com.loom.feature.home.impl

import androidx.compose.runtime.Composable
import com.loom.feature.home.impl.routes.FollowingFeedRoute
import com.loom.feature.home.impl.routes.ForYouRoute
import com.loom.feature.home.impl.routes.TagsRoute

@Composable
fun HomeTabPage(
    tab: HomeTab,
) {

    when (tab) {
        HomeTab.ForYou -> ForYouRoute()

        HomeTab.Following -> FollowingFeedRoute()

        HomeTab.Tags -> TagsRoute()

    }
}