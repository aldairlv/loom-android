package com.loom.feature.posteditor.api.navigation

import androidx.navigation3.runtime.NavKey
import com.loom.core.model.data.PostFeedItem
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostNavKey(
    val repostPost: PostFeedItem? = null
) : NavKey