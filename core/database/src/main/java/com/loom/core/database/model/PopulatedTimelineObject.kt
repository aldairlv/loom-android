package com.loom.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.loom.core.model.data.TimelineObject
import com.loom.core.model.enum.TimelineObjectType

data class PopulatedTimelineObject(
    @Embedded
    val entity: TimelineEntity,

    @Relation(
        parentColumn = "object_id",
        entityColumn = "id"
    )
    val post: PostEntity? = null,

    @Relation(
        parentColumn = "object_id",
        entityColumn = "id"
    )
    val title: TitleEntity? = null,

    @Relation(
        entity = CarouselEntity::class,
        parentColumn = "object_id",
        entityColumn = "id"
    )
    val carousel: PopulatedCarousel? = null
)

fun PopulatedTimelineObject.asExternalModel(): TimelineObject {
    return when (entity.objectType) {
        TimelineObjectType.POST.value -> TimelineObject.PostObject(
            content = post?.asExternalPostModel() ?: throw IllegalStateException("Post data missing")
        )
        TimelineObjectType.TITLE.value -> TimelineObject.TitleObject(
            content = title?.asExternalTitleModel() ?: throw IllegalStateException("Title data missing")
        )
        TimelineObjectType.CAROUSEL.value -> TimelineObject.CarouselObject(
            content = carousel?.asExternalCarouselModel() ?: throw IllegalStateException("Carousel data missing")
        )
        else -> throw IllegalArgumentException("Unknown type: ${entity.objectType}")
    }
}

