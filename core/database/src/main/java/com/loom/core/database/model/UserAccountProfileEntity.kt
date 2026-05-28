package com.loom.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loom.core.model.data.LocationCoords
import com.loom.core.model.data.UserAccountProfile

@Entity(tableName = "user_account_profiles")
data class UserAccountProfileEntity(
    @PrimaryKey val id: String,
    val user: String,
    val username: String,
    val displayName: String,
    val bio: String?,
    val city: String?,
    val timezone: String?,
    val canBeFollowed: Boolean?,
    val avatarUrl: String?,
    val bannerUrl: String?,
    @Embedded val locationCoords: LocationCoordsEntity?
)

data class LocationCoordsEntity(
    val latitude: Double,
    val longitude: Double
)

fun UserAccountProfileEntity.asExternalModel() = UserAccountProfile(
    id = id,
    user = user,
    username = username,
    displayName = displayName,
    bio = bio,
    city = city,
    timezone = timezone,
    canBeFollowed = canBeFollowed,
    avatarUrl = avatarUrl,
    bannerUrl = bannerUrl,
    locationCoords = locationCoords?.let {
        LocationCoords(
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
)

fun UserAccountProfile.asEntity() = UserAccountProfileEntity(
    id = id,
    user = user,
    username = username,
    displayName = displayName,
    bio = bio,
    city = city,
    timezone = timezone,
    canBeFollowed = canBeFollowed,
    avatarUrl = avatarUrl,
    bannerUrl = bannerUrl,
    locationCoords = locationCoords?.let {
        LocationCoordsEntity(
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
)
