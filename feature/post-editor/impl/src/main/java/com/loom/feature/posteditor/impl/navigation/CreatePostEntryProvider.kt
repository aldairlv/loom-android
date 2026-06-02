package com.loom.feature.posteditor.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.posteditor.api.navigation.CreatePostNavKey
import com.loom.feature.posteditor.impl.PostEditorScreen

fun EntryProviderScope<NavKey>.createPostEntry(navigator: Navigator) {
    entry<CreatePostNavKey> { key ->
        PostEditorScreen(
            repostPost = key.repostPost,
            onClose = { navigator.goBack() }
        )
    }
}