package com.loom.feature.events.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.events.api.navigation.EventsNavKey
import com.loom.feature.events.impl.EventsScreen
import com.loom.feature.eventeditor.api.navigation.CreateEventNavKey

fun EntryProviderScope<NavKey>.eventsEntry(navigator: Navigator) {
    entry<EventsNavKey> { key ->
        EventsScreen(
            navigator = navigator,
            onCreateClick = {
                navigator.navigate(CreateEventNavKey)
            }
        )
    }
}
