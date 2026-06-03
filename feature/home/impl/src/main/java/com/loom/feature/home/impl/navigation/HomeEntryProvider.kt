package com.loom.feature.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.comments.api.navigation.CommentsNavKey
import com.loom.feature.posteditor.api.navigation.CreatePostNavKey
import com.loom.feature.home.api.navigation.HomeNavKey
import com.loom.feature.home.impl.HomeScreen
import com.loom.core.model.data.PostFeedItem

fun EntryProviderScope<NavKey>.homeEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        HomeScreen(
            navigator = navigator,
            onCreateClick = {
                navigator.navigate(CreatePostNavKey())
            },
            onCommentClick = { postId ->
                navigator.navigate(CommentsNavKey(postId))
            },
            onRepostWithComment = { post ->
                navigator.navigate(CreatePostNavKey(repostPost = post))
            }
        )
    }
}
