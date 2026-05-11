package com.loom.core.model.data

sealed interface VerifyEmailResult {

    data object ExistingUser : VerifyEmailResult

    data object NewUser : VerifyEmailResult

    data class Error(
        val message: String
    ) : VerifyEmailResult
}