package com.loom.feature.comments.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class CommentsNavKey(val postId: String) : NavKey
