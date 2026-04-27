package com.loom.core.data.model

import com.loom.core.database.model.UserEntity
import com.loom.core.network.model.NetworkUser

fun NetworkUser.asInternalUserEntity() = UserEntity(
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
    uuid = uuid
)

