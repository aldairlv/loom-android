package com.loom.core.data.repository


import android.os.Build
import androidx.annotation.RequiresExtension
import com.loom.core.model.data.VerifyEmailResult
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkLoginRequest
import com.loom.core.network.model.NetworkRegisterErrorResponse
import com.loom.core.network.model.NetworkRegisterRequest
import kotlinx.serialization.json.Json
import javax.inject.Inject
import retrofit2.HttpException


internal class OfflineFirstAuthRepository @Inject constructor(
    private val network: LoomNetworkDataSource,
    private val userDataRepository: UserDataRepository,
    private val json: Json,
): AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val response = network.login(
                NetworkLoginRequest(email = email, password = password)
            )
            userDataRepository.setTokens(
                accessToken = response.access,
                refreshToken = response.refresh,
                userId = response.user.id
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyEmail(email: String): VerifyEmailResult {
        return try {
            val networkResponse = network.validateEmail(email)
            val data = networkResponse.response

            if (data.available) {
                VerifyEmailResult.NewUser
            } else {
                VerifyEmailResult.ExistingUser
            }

        } catch (e: Exception) {
            VerifyEmailResult.Error("Error de conexión: ${e.message}")
        }
    }


    override suspend fun register(
        username: String,
        email: String,
        password1: String,
        password2: String,
        birthDate: String
    ) : Result<Unit> {
        return try {
            val response = network.register(
                NetworkRegisterRequest(
                    username = username,
                    email = email,
                    password1 = password1,
                    password2 = password2,
                    birth_date = birthDate
                )
            )
            userDataRepository.setTokens(
                accessToken = response.access,
                refreshToken = response.refresh,
                userId = response.user.id
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            val errorMsg = parseRegisterError(e)
            Result.failure(Exception(errorMsg))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseRegisterError(e: HttpException): String {
        if (e.code() != 400) return "Error en el servidor (${e.code()})"

        return try {
            val errorBody = e.response()?.errorBody()?.string() ?: ""
            val parsed = json.decodeFromString<NetworkRegisterErrorResponse>(errorBody)

            // Buscamos el error específico de username
            when {
                parsed.username?.any { it.contains("exists", ignoreCase = true) } == true ->
                    "Ese nombre de usuario ya está en uso"
                else -> "Datos de registro inválidos"
            }
        } catch (_: Exception) {
            "Error desconocido al registrar"
        }
    }

}