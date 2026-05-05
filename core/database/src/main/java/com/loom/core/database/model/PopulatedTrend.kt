package com.loom.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.loom.core.model.data.TrendCategory
import com.loom.core.model.data.TrendCategoryItem
import com.loom.core.model.data.TrendCategoryItemTag
import com.loom.core.model.data.TrendCategoryItemVideo
import com.loom.core.model.enum.TrendType

data class PopulatedTrend(
    @Embedded val entity: TrendEntity,

    @Relation(
        entity = TrendItemEntity::class,
        parentColumn = "id",
        entityColumn = "trend_id"
    )
    val items: List<PopulatedTrendItem>
)

data class PopulatedTrendItem(
    @Embedded val itemEntity: TrendItemEntity,

    @Relation(
        entity = PostEntity::class,
        parentColumn = "post_id",
        entityColumn = "id"
    )
    val post: PostEntity? = null,

    @Relation(
        entity = TagEntity::class,
        parentColumn = "tag_id",
        entityColumn = "name"
    )
    val tag: TagEntity? = null
)

fun PopulatedTrendItem.asExternalTrendItemModel(): TrendCategoryItem {
    return when (itemEntity.objectType) {
        "tag" -> {
            val tagData = tag ?: error("Tag data missing for ${itemEntity.tagId}")
            val postData = post ?: error("Post data missing for ${itemEntity.postId}")

            TrendCategoryItemTag(
                id = itemEntity.id,
                objectType = itemEntity.objectType,
                tag = tagData.asExternalTagModel(),
                resource = listOf(postData.asExternalPostModel())
            )
        }

        "video" -> {
            val postData = post ?: error("Post data missing for item ${itemEntity.id}")
            TrendCategoryItemVideo(
                id = itemEntity.id,
                objectType = itemEntity.objectType,
                resource = listOf(postData.asExternalPostModel())
            )
        }

        else -> error("Unknown trend item type: ${itemEntity.objectType}")
    }
}

fun PopulatedTrend.asExternalTrendModel(): TrendCategory {
    return TrendCategory(
        id = entity.id,
        category = entity.category,
        count = entity.count,
        iconUrl = entity.iconUrl,
        subType = when(entity.subType) {
            "tag" -> TrendType.TAG
            "video" -> TrendType.VIDEO
            else -> error("Unknown trend type: ${entity.subType}")
        },
        items = items.sortedBy { it.itemEntity.position }.map { it.asExternalTrendItemModel() }
    )
}

