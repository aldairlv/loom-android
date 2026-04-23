package com.loom.feature.foryou.impl.navigation


import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator

import com.loom.feature.foryou.api.navigation.ForYouNavKey
import com.loom.feature.foryou.impl.ForYouScreen


fun EntryProviderScope<NavKey>.forYouEntry(navigator: Navigator) {
    entry<ForYouNavKey> {
        // TODO: Aquí cargamos la pantalla de For You
        ForYouScreen()
    }
}