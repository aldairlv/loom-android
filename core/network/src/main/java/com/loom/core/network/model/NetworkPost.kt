package com.loom.core.network.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPost(
    val id: String,
    val blogId: Int,
    val username: String,
    val timestamp: Long,
    val tags: List<String>,
    val content: List<NetworkContentObject>,
    val likesCount: Int,
    val repostsCount: Int,
    val commentsCount: Int,
    val notesCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)