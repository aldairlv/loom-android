package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("post")
data class NetworkObjectPost(
    override val objectType: String,
    override val id: String,
    override val streamGlobalPosition: Int,
    override val streamSessionId: String? = null,
    val author: NetworkPostAuthor,
    val parent: NetworkPostParent? = null,
    val root: NetworkPostParent? = null,
    val trail: List<NetworkPostFeedItem> = emptyList(),
    val status: String = "",
    val tags: List<String> = emptyList(),
    val contents: List<NetworkPostContent> = emptyList(),
    val layout: List<NetworkLayoutRoot> = emptyList(),
    val interactions: NetworkPostInteractions? = null,
    val stats: NetworkPostStats? = null,
    val is_deleted: Boolean = false,
    val created_at: String = "",
    val updated_at: String = "",
    val published_at: String? = null
) : NetworkObject
