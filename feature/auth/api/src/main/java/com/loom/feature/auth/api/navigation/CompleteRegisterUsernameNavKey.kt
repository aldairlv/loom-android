package com.loom.feature.auth.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CompleteRegisterUsernameNavKey(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val birthMonth: String,
    val birthDay: String,
    val birthYear: String
): NavKey {
}
