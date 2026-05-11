package com.loom.feature.auth.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.loom.core.navigation.Navigator
import com.loom.feature.auth.api.navigation.PasswordInputNavKey
import com.loom.feature.auth.impl.PasswordInputScreen
import com.loom.feature.foryou.api.navigation.ForYouNavKey
import com.loom.feature.auth.api.navigation.CompleteRegisterBirthDateNavKey

fun EntryProviderScope<NavKey>.passwordInputEntry(navigator: Navigator) {
    entry<PasswordInputNavKey> {key ->
        PasswordInputScreen(
            email = key.email,
            isExistingUser = key.isExistingUser,
            onBackClick = { navigator.goBack() },
            onNextClickRegister = {
                email,
                password,
                confirmPassword -> navigator.navigate(
                CompleteRegisterBirthDateNavKey(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword
                    )
                )
                                  },
            onClickLogin = { navigator.resetTo(ForYouNavKey) }
            //onClickLogin = { navigator.navigate(ForYouNavKey) }
        )
    }
}