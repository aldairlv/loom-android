package com.loom.core.data.model

import com.loom.core.database.model.CarouselEntity
import com.loom.core.database.model.CarouselItemEntity
import com.loom.core.network.model.NetworkObjectCarousel
import com.loom.core.network.model.NetworkCarouselElementObjectUser
import com.loom.core.network.model.NetworkCarouselElementObjectEvent

fun NetworkObjectCarousel.asInternalCarouselEntity(): CarouselEntity {
    return CarouselEntity(
        id = id,
        objectType = objectType
    )
}

fun NetworkObjectCarousel.asInternalCarouselItemEntities(): List<CarouselItemEntity> {
    return elements.mapIndexed { index, element ->

        when (element) {

            is NetworkCarouselElementObjectUser -> {
                val user = element.resource.firstOrNull()
                    ?: error("User resource is empty")

                CarouselItemEntity(
                    carouselId = id,
                    objectId = user.id,
                    objectType = "user",
                    position = index
                )
            }

            is NetworkCarouselElementObjectEvent -> {
                val event = element.resource.firstOrNull()
                    ?: error("Event resource is empty")

                CarouselItemEntity(
                    carouselId = id,
                    objectId = event.id.toString(),
                    objectType = "event",
                    position = index
                )
            }
        }
    }
}

