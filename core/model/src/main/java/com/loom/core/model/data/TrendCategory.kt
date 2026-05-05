package com.loom.core.model.data

import com.loom.core.model.enum.TrendType
import kotlinx.serialization.Serializable

@Serializable
data class TrendCategory(
    val id: String,
    val category: String,
    val iconUrl: String,
    val count: String,
    val subType: TrendType,
    val items: List<TrendCategoryItem>
)