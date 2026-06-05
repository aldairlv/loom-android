package com.loom.core.data.repository

import com.loom.core.model.data.NotificationsResult
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(cursor: String? = null): Flow<NotificationsResult>
}