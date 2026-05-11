package com.loom.feature.auth.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.auth.api.navigation.EmailInputNavKey
import com.loom.feature.auth.api.navigation.PasswordInputNavKey
import com.loom.feature.auth.impl.EmailInputScreen

fun EntryProviderScope<NavKey>.emailInputEntry(navigator: Navigator) {
    entry<EmailInputNavKey> {
        EmailInputScreen(
            onBackClick = { navigator.goBack() },
            onNextClick = { email, isExistingUser ->
                navigator.navigate(
                    PasswordInputNavKey(
                        email = email,
                        isExistingUser = isExistingUser
                    )
                )
            }
        )
    }
}