package com.loom.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.settings.api.navigation.AccountSettingsNavKey
import com.loom.feature.settings.api.navigation.LocationNavKey
import com.loom.feature.settings.impl.AccountSettingsScreen

fun EntryProviderScope<NavKey>.accountSettingsEntry(navigator: Navigator) {
    entry<AccountSettingsNavKey> {
        AccountSettingsScreen(
            onBackClick = { navigator.goBack() },
            onLocationClick = { navigator.navigate(LocationNavKey) }
        )
    }
}
