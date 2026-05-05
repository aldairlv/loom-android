package com.loom.core.model.data

import com.loom.core.model.enum.TimelineObjectType
import kotlinx.serialization.Serializable


sealed interface TrendCategoryItem{
    val id: String
    val objectType: String
}

data class TrendCategoryItemTag(
    override val id: String,
    override val objectType: String,
    val tag: TagProfile,
    val resource: List<Post>
): TrendCategoryItem

data class TrendCategoryItemVideo(
    override val id: String,
    override val objectType: String,
    val resource: List<Post>
): TrendCategoryItem

