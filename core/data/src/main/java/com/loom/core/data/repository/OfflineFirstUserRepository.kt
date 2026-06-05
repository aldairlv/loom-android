package com.loom.core.data.repository

import com.loom.core.common.util.DeviceIdProvider
import com.loom.core.database.dao.UserAccountProfileDao
import com.loom.core.database.model.asEntity
import com.loom.core.database.model.asExternalModel
import com.loom.core.model.data.LocationCoords
import com.loom.core.model.data.UserAccountProfile
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkDeviceRequest
import com.loom.core.network.model.NetworkLocationCoords
import com.loom.core.network.model.NetworkUserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class OfflineFirstUserRepository @Inject constructor(
    private val userAccountProfileDao: UserAccountProfileDao,
    private val network: LoomNetworkDataSource,
    private val deviceIdProvider: DeviceIdProvider,
) : UserRepository {

    override fun getUserAccountProfile(id: String): Flow<UserAccountProfile?> {
        return userAccountProfileDao.getUserAccountProfile(id)
            .map { it?.asExternalModel() }
    }

    override fun getUserAccountProfileByUserId(userId: String): Flow<UserAccountProfile?> {
        return userAccountProfileDao.getUserAccountProfileByUserId(userId)
            .map { it?.asExternalModel() }
    }

    override suspend fun syncUserAccountProfile(id: String) {
        val networkProfile = network.getUserProfile(id)
        userAccountProfileDao.insertOrUpdateUserAccountProfile(networkProfile.asExternalModel().asEntity())
    }

    override suspend fun syncMyProfile() {
        val networkProfile = network.getMyProfile()
        userAccountProfileDao.insertOrUpdateUserAccountProfile(networkProfile.asExternalModel().asEntity())
    }

    override suspend fun registerDevice(deviceId: String, fcmToken: String?, isActive: Boolean) {
        network.registerDevice(
            NetworkDeviceRequest(
                deviceId = deviceId,
                registrationToken = fcmToken,
                deviceType = if (isActive) "android" else null,
                isActive = isActive
            )
        )
    }
}

private fun NetworkUserProfile.asExternalModel() = UserAccountProfile(
    id = id,
    user = user,
    username = username,
    displayName = display_name,
    bio = bio,
    city = city,
    timezone = timezone,
    canBeFollowed = can_be_followed,
    avatarUrl = avatar_url,
    bannerUrl = banner_url,
    locationCoords = location_coords?.asExternalModel()
)

private fun NetworkLocationCoords.asExternalModel() = LocationCoords(
    latitude = latitude,
    longitude = longitude
)
