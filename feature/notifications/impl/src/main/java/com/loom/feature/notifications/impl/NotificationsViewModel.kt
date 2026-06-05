package com.loom.feature.notifications.impl

import androidx.lifecycle.ViewModel
import com.loom.core.ui.tabs.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {
    private val _tabs = MutableStateFlow(
        listOf(
            TabItem("activity", "Activity"),
            TabItem("chats", "Chats"),
            TabItem("ask", "Ask"),
        )
    )
    val tabs: StateFlow<List<TabItem>> = _tabs.asStateFlow()

    fun toggleTab(key: String, enabled: Boolean) {
        _tabs.update { currentTabs ->
            currentTabs.map {
                if (it.key == key) it.copy(enabled = enabled) else it
            }
        }
    }
}
