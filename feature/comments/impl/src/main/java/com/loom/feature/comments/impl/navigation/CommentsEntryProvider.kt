package com.loom.feature.comments.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.comments.api.navigation.CommentsNavKey
import com.loom.feature.comments.impl.CommentsScreen

fun EntryProviderScope<NavKey>.commentsEntry(navigator: Navigator) {
    entry<CommentsNavKey> { key ->
        CommentsScreen(
            postId = key.postId,
            onClose = { navigator.goBack() }
        )
    }
}
