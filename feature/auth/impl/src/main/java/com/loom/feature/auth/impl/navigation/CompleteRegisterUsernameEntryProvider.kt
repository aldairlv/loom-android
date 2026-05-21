package com.loom.feature.auth.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.auth.api.navigation.CompleteRegisterUsernameNavKey
import com.loom.feature.auth.impl.CompleteRegisterUsernameScreen
import com.loom.feature.home.api.navigation.HomeNavKey


fun EntryProviderScope<NavKey>.completeRegisterUsernameEntry(navigator: Navigator) {
    entry< CompleteRegisterUsernameNavKey> { key ->
        CompleteRegisterUsernameScreen(
            email = key.email,
            password = key.password,
            confirmPassword = key.confirmPassword,
            birthMonth = key.birthMonth,
            birthDay = key.birthDay,
            birthYear = key.birthYear,
            onBackClick = { navigator.goBack() },
            onRegisterSuccess = { navigator.resetTo(HomeNavKey) }
        )
    }
}