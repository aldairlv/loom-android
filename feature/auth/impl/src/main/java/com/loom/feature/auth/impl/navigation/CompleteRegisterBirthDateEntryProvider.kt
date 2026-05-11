package com.loom.feature.auth.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.auth.api.navigation.CompleteRegisterBirthDateNavKey

import com.loom.feature.auth.api.navigation.CompleteRegisterUsernameNavKey
import com.loom.feature.auth.impl.CompleteRegisterBirthDateScreen

fun EntryProviderScope<NavKey>.completeRegisterBirthDateEntry(navigator: Navigator) {
    entry< CompleteRegisterBirthDateNavKey> { key ->
        CompleteRegisterBirthDateScreen(
            email = key.email,
            password = key.password,
            confirmPassword = key.confirmPassword,
            onBackClick = { navigator.goBack() },
            onNextClickRegister = {
                email,
                password,
                confirmPassword,
                birthMonth,
                birthDay,
                birthYear -> navigator.navigate(
                CompleteRegisterUsernameNavKey(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        birthMonth = birthMonth,
                        birthDay = birthDay,
                        birthYear = birthYear
                    )
                )},
        )
    }
}