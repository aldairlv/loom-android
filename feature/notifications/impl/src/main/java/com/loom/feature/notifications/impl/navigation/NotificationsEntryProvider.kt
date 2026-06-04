package com.loom.feature.notifications.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.notifications.api.navigation.NotificationsNavKey
import com.loom.feature.notifications.impl.NotificationsScreen


fun EntryProviderScope<NavKey>.notificationsEntry(navigator: Navigator) {
    entry<NotificationsNavKey> {
        NotificationsScreen()
    }
}
