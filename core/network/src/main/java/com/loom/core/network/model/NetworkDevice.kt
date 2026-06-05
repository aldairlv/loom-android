package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkDeviceRequest(
    @SerialName("device_id") val deviceId: String,
    @SerialName("registration_token") val registrationToken: String? = null,
    @SerialName("device_type") val deviceType: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null
)

@Serializable
data class NetworkDeviceResponse(
    val id: String? = null,
    @SerialName("device_id") val deviceId: String? = null,
    @SerialName("registration_token") val registrationToken: String? = null,
    @SerialName("device_type") val deviceType: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null
)
