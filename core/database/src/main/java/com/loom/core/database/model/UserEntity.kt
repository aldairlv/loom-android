package com.loom.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loom.core.model.data.UserProfile

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val avatar: String,
    val userViewUrl: String,
    val canBeFollowed: Boolean,
    val canShowBages: Boolean,
    val description: String,
    val followed: Boolean,
    val isAdult: Boolean,
    val title: String,
    val uuid: String
)

fun UserEntity.asExternalModel() = UserProfile(
    id = id,
    username = username,
    avatar = avatar,
    userViewUrl = userViewUrl,
    canBeFollowed = canBeFollowed,
    canShowBages = canShowBages,
    description = description,
    followed = followed,
    isAdult = isAdult,
    title = title,
    uuid = uuid,
    posts = emptyList() // Al ser la entidad básica, no tiene posts cargados
)