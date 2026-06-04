package com.loom.feature.eventeditor.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.eventeditor.api.navigation.CreateEventNavKey
import com.loom.feature.eventeditor.impl.EventEditorScreen

fun EntryProviderScope<NavKey>.createEventEntry(navigator: Navigator) {
    entry<CreateEventNavKey> { key ->
        EventEditorScreen(
            onClose = { navigator.goBack() }
        )
    }
}