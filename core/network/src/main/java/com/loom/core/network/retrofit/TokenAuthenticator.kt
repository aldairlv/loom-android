package com.loom.core.network.retrofit

import com.loom.core.network.LoomNetworkDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject


internal class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val networkDataSource: dagger.Lazy<LoomNetworkDataSource>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. Obtenemos el refresh token de forma sincrónica
        val refreshToken = runBlocking { tokenManager.getRefreshToken() }

        if (refreshToken.isNullOrBlank()) return null

        // 2. Intentamos refrescar el token
        return synchronized(this) {
            runBlocking {
                // Comprobamos si el token ya fue refrescado por otra petición paralela
                val currentToken = tokenManager.accessToken.first()
                val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

                if (currentToken != requestToken) {
                    // El token ya cambió, reintentamos con el nuevo
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                }

                try {
                    val tokenResponse = networkDataSource.get().refreshToken(refreshToken)
                    val newAccessToken = tokenResponse.access

                    // El backend no devuelve un nuevo refresh token aquí, así que mantenemos el actual
                    tokenManager.updateTokens(newAccessToken, refreshToken)

                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } catch (e: Exception) {
                    // Si el refresh falla (ej. refresh token expirado), cerramos sesión
                    tokenManager.clearTokens()
                    null
                }
            }
        }
    }
}