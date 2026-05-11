package com.loom.feature.auth.impl

data class PasswordValidationState(
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)