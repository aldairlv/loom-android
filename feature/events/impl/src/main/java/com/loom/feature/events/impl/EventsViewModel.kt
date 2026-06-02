package com.loom.feature.events.impl

import androidx.lifecycle.ViewModel
import com.loom.core.model.data.FeedObject
import com.loom.core.ui.tabs.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    //private val eventsRepository: EventsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Success(emptyList()))
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    private val _tabs = MutableStateFlow(
        listOf(
            TabItem("upcoming", "Próximos"),
            TabItem("today", "Hoy"),
            TabItem("tomorrow", "Mañana"),
            TabItem("weekend", "Fin de semana"),
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

    fun onClickLike(postId: String) {}
    fun onQuickRepost(postId: String) {}
    fun onShare(postId: String) {}
    fun onFollowClick(userId: String, username: String, isFollowing: Boolean) {}
}

sealed interface EventsUiState {
    object Loading : EventsUiState
    data class Success(val events: List<FeedObject>) : EventsUiState
    data class Error(val message: String) : EventsUiState
}

