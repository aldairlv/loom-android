package com.loom.feature.home.impl

import com.loom.core.ui.tabs.TabItem


sealed interface HomeTab {

    val key: String
    val title: String

    data object ForYou : HomeTab {
        override val key = "foryou"
        override val title = "For You"
    }

    data object Following : HomeTab {
        override val key = "following"
        override val title = "Following"
    }

    data object Tags : HomeTab {
        override val key = "tags"
        override val title = "Tags"
    }
}

fun HomeTab.toTabItem(enabled: Boolean = true) =
    TabItem(key = key, title = title, enabled = enabled)