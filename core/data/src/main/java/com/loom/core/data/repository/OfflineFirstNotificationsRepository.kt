package com.loom.core.data.repository

import com.loom.core.network.LoomNetworkDataSource
import javax.inject.Inject

internal class OfflineFirstNotificationsRepository @Inject constructor(
    private val network: LoomNetworkDataSource,
): NotificationsRepository {
}