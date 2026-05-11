package com.loom.core.data.repository

import com.loom.core.model.data.VerifyEmailResult
import com.loom.core.network.model.NetworkAuthResponse
import com.loom.core.network.model.NetworkRegisterRequest

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun verifyEmail(email: String): VerifyEmailResult

    suspend fun register(
        username: String,
        email: String,
        password1: String,
        password2: String,
        birthDate: String
    ): Result<Unit>
}
