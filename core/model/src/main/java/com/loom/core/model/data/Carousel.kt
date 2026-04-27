package com.loom.core.model.data

import kotlinx.serialization.Serializable


sealed interface CarouselElement {
    val id: String
    val objectType: String

}

@Serializable
data class CarouselUserElement(
    override val id: String,
    override val objectType: String,
    val resource: List<UserProfile>
) : CarouselElement

@Serializable
data class CarouselEventElement(
    override val id: String,
    override val objectType: String,
    val resource: List<EventProfile>
) : CarouselElement

@Serializable
data class Carousel(
    val id: String,
    val type: String, // "blog", "event", "mixed"
    val elements: List<CarouselElement>
)
