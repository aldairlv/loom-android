package com.loom.feature.auth.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class PasswordInputNavKey(
    val email: String,
    val isExistingUser: Boolean
): AuthNavKey {
}