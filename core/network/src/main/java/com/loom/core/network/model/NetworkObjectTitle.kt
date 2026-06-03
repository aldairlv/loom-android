package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("title")
data class NetworkObjectTitle(
    override val objectType: String = "title",
    override val id: String,
    override val streamGlobalPosition: Int? = null,
    override val streamSessionId: String? = null,
    val text: String
) : NetworkObject