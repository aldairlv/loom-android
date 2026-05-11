package com.loom.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkAuthResponse(
    val access: String,
    val refresh: String,
    val user: NetworkAuthUser
)

@Serializable
data class NetworkAuthUser(
    val id: String,
    val email: String,
    val username: String,
    val date_joined: String,
    val birth_date: String? = null

)

@Serializable
data class NetworkTokenResponse(
    val access: String,
    val access_expiration: String? = null
)

@Serializable
data class NetworkLoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class NetworkRegisterRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String,
    val birth_date: String
)

@Serializable
data class NetworkRefreshRequest(
    val refresh: String
)

@Serializable
data class NetworkLogoutResponse(
    val detail: String
)
@Serializable
data class NetworkValidateEmailRequest(
    val email: String
)

@Serializable
data class NetworkValidateEmailResponse(
    val meta: NetworkMeta,
    val response: NetworkValidateEmailData
)

@Serializable
data class NetworkMeta(
    val status: Int,
    val msg: String
)

@Serializable
data class NetworkValidateEmailData(
    val available: Boolean,
    val message: String? = null,
)

@Serializable
data class NetworkRegisterErrorResponse(
    val username: List<String>? = null
)