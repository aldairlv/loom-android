package com.loom.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimelineObject {
    val id: String // Todos los objects deberían tener un ID para el DiffUtil de Compose

    @Serializable
    @SerialName("post_type")
    data class PostObject(
        val content: Post
    ) : TimelineObject {
        override val id: String = "post_${content.id}"
    }

    @Serializable
    @SerialName("title_type")
    data class TitleObject(
        val content: Title
    ) : TimelineObject {
        override val id: String = "title_${content.id}"
    }

    @Serializable
    @SerialName("carousel_type")
    data class CarouselObject(
        val content: Carousel
    ) : TimelineObject {
        override val id: String = "carousel_${content.id}"
    }
}

// Clase auxiliar para el carrusel
@Serializable
data class UserPreview(
    val userId: String,
    val username: String,
    val avatarUrl: String
)