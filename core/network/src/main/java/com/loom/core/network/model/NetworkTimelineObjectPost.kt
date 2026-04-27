package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
@SerialName("post")
data class NetworkTimelineObjectPost(
    override val objectType: String,
    override val id: String,
    override val streamGlobalPosition: Int,
    val blogId: Int,
    val username: String,
    val timestamp: Long,
    val likesCount: Int,
    val repostsCount: Int,
    val commentsCount: Int,
    val notesCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
    val tags: List<String>,
    val content: List<NetworkContentObject>
) : NetworkTimelineObject