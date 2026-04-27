package com.loom.core.model.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Title(
    val id: String,
    val text: String,
)