package com.loom.core.data.model

import com.loom.core.database.model.TimelineEntity
import com.loom.core.network.model.NetworkObject

fun NetworkObject.asInternalTimelineEntity(timelineCategory: String): TimelineEntity {
    return TimelineEntity(
        timelineCategory = timelineCategory,  // ej: "dashboard", "for_you", "following", "tags"
        objectType = this.objectType, // ej: "post", "carousel", "title"
        objectId = this.id, // ID del Post, Carousel, etc.
        streamGlobalPosition = this.streamGlobalPosition  // Orden que manda el backend durante la sesión
    )
}

