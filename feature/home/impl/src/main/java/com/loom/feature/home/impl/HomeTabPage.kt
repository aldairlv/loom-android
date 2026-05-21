package com.loom.feature.home.impl

import androidx.compose.runtime.Composable
import com.loom.feature.home.impl.routes.FollowingRoute
import com.loom.feature.home.impl.routes.ForYouRoute
import com.loom.feature.home.impl.routes.TagsRoute

@Composable
fun HomeTabPage(
    tab: HomeTab,
) {

    when (tab) {
        HomeTab.ForYou -> ForYouRoute()

        HomeTab.Following -> FollowingRoute()

        HomeTab.Tags -> TagsRoute()

    }
}