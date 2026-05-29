package com.loom.feature.settings.impl.navigation

import java.util.Map.entry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.settings.api.navigation.AccountSettingsNavKey
import com.loom.feature.settings.api.navigation.SettingsNavKey
import com.loom.feature.settings.impl.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry(navigator: Navigator) {
    entry<SettingsNavKey> {
        SettingsScreen(
            onBackClick = { navigator.goBack() },
            onAccountSettingsClick = {navigator.navigate(AccountSettingsNavKey)}
        )
    }
}