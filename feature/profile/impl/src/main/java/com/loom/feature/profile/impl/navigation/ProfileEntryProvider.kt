package com.loom.feature.profile.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.posteditor.api.navigation.CreatePostNavKey
import com.loom.feature.profile.api.navigation.ProfileNavKey
import com.loom.feature.profile.impl.ProfileScreen
import com.loom.feature.settings.api.navigation.SettingsNavKey

fun EntryProviderScope<NavKey>.profileEntry(navigator: Navigator) {
    entry<ProfileNavKey> {
        ProfileScreen(
            onCreateClick = {
                navigator.navigate(CreatePostNavKey)
            },
            onSettingsClick = {
                navigator.navigate(SettingsNavKey)
            }
        )
    }
}