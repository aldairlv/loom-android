package com.loom.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "timeline_objects",
    primaryKeys = ["timeline_category", "object_id", "object_type"]
)
data class TimelineEntity(
    @ColumnInfo(name = "timeline_category")
    val timelineCategory: String, // ej: "dashboard", "for_you", "following", "tags"

    @ColumnInfo(name = "object_type")
    val objectType: String, // ej: "post", "carousel", "title"

    @ColumnInfo(name = "object_id")
    val objectId: String, // ID del Post, Carousel, etc.

    @ColumnInfo(name = "stream_global_position")
    val streamGlobalPosition: Int, // Orden que manda el backend durante la sesión

    @ColumnInfo(name = "stream_session_id")
    val streamSessionId: String? = null
)
