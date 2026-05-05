package com.loom.core.database.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loom.core.model.data.TagProfile

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey
    val name: String,
    val isFollowed: Boolean
)

fun TagEntity.asExternalTagModel() = TagProfile(
    name = name,
    isFollowed = isFollowed
)