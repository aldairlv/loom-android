package com.loom.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carousels")
data class CarouselEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "object_type")
    val objectType: String // Carousel category: "user", "event", etc.
)

@Entity(
    tableName = "carousel_items",
    primaryKeys = ["carousel_id", "object_id"]
)
data class CarouselItemEntity(
    @ColumnInfo(name = "carousel_id")
    val carouselId: String,

    @ColumnInfo(name = "object_id")
    val objectId: String,

    @ColumnInfo(name = "object_type")
    val objectType: String, // "user" o "event"

    val position: Int
)
