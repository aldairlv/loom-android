package com.loom.core.data.model

import com.loom.core.database.model.PopulatedTrend
import com.loom.core.database.model.TagEntity
import com.loom.core.database.model.TrendEntity
import com.loom.core.database.model.TrendItemEntity
import com.loom.core.network.model.NetworkObjectTrend
import com.loom.core.network.model.NetworkObjectTrendElementTag
import com.loom.core.network.model.NetworkObjectTrendElementVideo
fun NetworkObjectTrend.asInternalTrendEntity(): TrendEntity{
    return TrendEntity(
        id = id,
        objectType = objectType,
        category = category,
        count = count,
        iconUrl = iconUrl,
        subType = subType
    )
}

fun NetworkObjectTrend.asInternalTrendItemEntities(): List<TrendItemEntity>{
    return elements.mapIndexed { index, element ->
        when (element) {
            is NetworkObjectTrendElementTag -> {
                val post = element.resource.firstOrNull()
                    ?: error("Tag resource is empty")

                TrendItemEntity(
                    id = element.id,
                    trendId = id,
                    postId = post.id,
                    tagId = element.name,
                    objectType = element.objectType,
                    position = index
                )
            }
            is NetworkObjectTrendElementVideo -> {
                val post = element.resource.firstOrNull()?:
                error("Video resource is empty")

                TrendItemEntity(
                    id = element.id,
                    trendId = id,
                    postId = post.id,
                    tagId = null,
                    objectType = element.objectType,
                    position = index
                )

            }
        }
    }
}


fun NetworkObjectTrendElementTag.asInternalTrendTagEntity(): TagEntity{
    return TagEntity(
        name = name,
        isFollowed = isFollowed
    )
}

