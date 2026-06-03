package com.loom.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.settings.api.navigation.LocationNavKey
import com.loom.feature.settings.impl.LocationScreen

fun EntryProviderScope<NavKey>.locationEntry(navigator: Navigator) {
    entry<LocationNavKey> {
        LocationScreen(
            onBackClick = { navigator.goBack() }
        )
    }
}
