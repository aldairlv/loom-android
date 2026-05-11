package com.loom.feature.auth.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.auth.api.navigation.EmailInputNavKey
import com.loom.feature.auth.api.navigation.LandingNavKey
import com.loom.feature.auth.impl.AuthViewModel
import com.loom.feature.auth.impl.LandingScreen

fun EntryProviderScope<NavKey>.landingEntry(navigator: Navigator) {
    entry<LandingNavKey> {
        LandingScreen(
            onContinueEmail = {
                navigator.navigate(EmailInputNavKey)
            }
        )
    }
}