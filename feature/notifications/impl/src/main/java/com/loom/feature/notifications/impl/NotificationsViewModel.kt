package com.loom.feature.notifications.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.ui.tabs.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    private val _tabs = MutableStateFlow(
        listOf(
            TabItem("activity", "Activity"),
            TabItem("chats", "Chats"),
            TabItem("ask", "Ask"),
        )
    )
    val tabs: StateFlow<List<TabItem>> = _tabs.asStateFlow()

    val shouldShowPermissionSnackbar: StateFlow<Boolean> = userDataRepository.userData
        .map { userData ->
            val oneHourMillis = 60 * 60 * 1000L
            val currentTime = System.currentTimeMillis()
            currentTime - userData.lastNotificationPermissionRequestTime > oneHourMillis
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun toggleTab(key: String, enabled: Boolean) {
        _tabs.update { currentTabs ->
            currentTabs.map {
                if (it.key == key) it.copy(enabled = enabled) else it
            }
        }
    }

    fun onPermissionRequested() {
        viewModelScope.launch {
            userDataRepository.setLastNotificationPermissionRequestTime(System.currentTimeMillis())
        }
    }

    fun onDismissPermissionRequest() {
        viewModelScope.launch {
            userDataRepository.setLastNotificationPermissionRequestTime(System.currentTimeMillis())
        }
    }
}
