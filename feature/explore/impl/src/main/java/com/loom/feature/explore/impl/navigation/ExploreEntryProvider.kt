package com.loom.feature.explore.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator

import com.loom.feature.explore.api.navigation.ExploreNavKey
import com.loom.feature.explore.impl.ExploreScreen
fun EntryProviderScope<NavKey>.exploreEntry(navigator: Navigator) {
    entry<ExploreNavKey> {
        ExploreScreen()
    }
}