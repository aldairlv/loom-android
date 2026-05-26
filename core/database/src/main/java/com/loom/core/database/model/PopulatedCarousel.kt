package com.loom.core.database.model

/*
import androidx.room.Embedded
import androidx.room.Relation
import com.loom.core.model.data.Carousel
import com.loom.core.model.data.CarouselElement
import com.loom.core.model.data.CarouselEventElement
import com.loom.core.model.data.CarouselUserElement

data class PopulatedCarousel(
    @Embedded val entity: CarouselEntity,

    @Relation(
        entity = CarouselItemEntity::class,
        parentColumn = "id",
        entityColumn = "carousel_id"
    )
    val items: List<PopulatedCarouselItem>
)

data class PopulatedCarouselItem(
    @Embedded val itemEntity: CarouselItemEntity,

    @Relation(
        entity = UserEntity::class,
        parentColumn = "object_id",
        entityColumn = "id"
    )
    val userProfile: PopulatedUserProfile? = null,

    @Relation(
        entity = EventEntity::class,
        parentColumn = "object_id",
        entityColumn = "id"
    )
    val eventProfile: PopulatedEventProfile? = null
)

fun PopulatedCarouselItem.asExternalCarouselItemModel(): CarouselElement {
    return when (itemEntity.objectType) {
        "user" -> {
            val user = userProfile ?: error("UserProfile data missing for item ${itemEntity.objectId}")

            CarouselUserElement(
                id = itemEntity.objectId,
                objectType = "user",
                resource = listOf(user.asExternalUserProfileModel())
            )
        }
        "event" -> {
            val event = eventProfile ?: error("EventProfile data missing for item ${itemEntity.objectId}")

            CarouselEventElement(
                id = itemEntity.objectId,
                objectType = "event",
                resource = listOf(event.asExternalEventProfileModel())
            )
        }
        else -> error("Unknown carousel item type: ${itemEntity.objectType}")
    }
}

fun PopulatedCarousel.asExternalCarouselModel() = Carousel(
    id = entity.id,
    type = entity.objectType,
    elements = items
        .sortedBy { it.itemEntity.position }
        .map { it.asExternalCarouselItemModel() }
)
*/
