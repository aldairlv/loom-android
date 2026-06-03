package com.loom.feature.eventdetail.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.eventdetail.api.navigation.EventNavKey
import com.loom.feature.eventdetail.impl.EventScreen

fun EntryProviderScope<NavKey>.eventEntry(navigator: Navigator) {
    entry<EventNavKey> { key ->
        EventScreen(
            onBackClick = { navigator.goBack() }
        )
    }
}
