package com.loom.feature.notifications.impl

import androidx.lifecycle.ViewModel
import com.loom.core.data.repository.NotificationsRepository
import com.loom.core.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
) : ViewModel() {

}