package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("title")
data class NetworkTimelineObjectTitle(
    override val objectType: String,
    override val id: String,
    override val streamGlobalPosition: Int,
    val text: String
) : NetworkTimelineObject