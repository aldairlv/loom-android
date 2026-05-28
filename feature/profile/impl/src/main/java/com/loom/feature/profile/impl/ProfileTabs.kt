package com.loom.feature.profile.impl

import com.loom.core.ui.tabs.TabItem

sealed interface ProfileTab {

    val key: String
    val title: String

    data object Posts : ProfileTab {
        override val key = "posts"
        override val title = "Posts"
    }

    data object Likes : ProfileTab {
        override val key = "likes"
        override val title = "Likes"
    }

    data object FollowingUsers : ProfileTab {
        override val key = "followingUsers"
        override val title = "Following"
    }
}

fun ProfileTab.toTabItem(enabled: Boolean = true) =
    TabItem(key = key, title = title, enabled = enabled)