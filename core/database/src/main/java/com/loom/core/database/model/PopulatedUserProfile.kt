package com.loom.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.loom.core.model.data.UserProfile

data class PopulatedUserProfile(
    @Embedded val user: UserEntity,

    @Relation(
        parentColumn = "username",
        entityColumn = "username"
    )
    val posts: List<PostEntity>
)

/**
 * Mapper para convertir el modelo de BD al modelo de dominio (External Model)
 */
fun PopulatedUserProfile.asExternalUserProfileModel() = UserProfile(
    id = user.id,
    username = user.username,
    avatar = user.avatar,
    userViewUrl = user.userViewUrl,
    canBeFollowed = user.canBeFollowed,
    canShowBages = user.canShowBages,
    description = user.description,
    followed = user.followed,
    isAdult = user.isAdult,
    title = user.title,
    uuid = user.uuid,
    posts = posts.map { it.asExternalPostModel() }
)