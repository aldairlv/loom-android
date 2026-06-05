package com.loom.feature.notifications.impl.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.NotificationsRepository
import com.loom.core.model.data.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ActivityUiState {
    object Loading : ActivityUiState
    data class Success(
        val notifications: List<Notification>,
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val nextCursor: String? = null
    ) : ActivityUiState
    data class Error(val message: String?) : ActivityUiState
}

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ActivityUiState>(ActivityUiState.Loading)
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = ActivityUiState.Loading
            notificationsRepository.getNotifications(null)
                .catch { e -> _uiState.value = ActivityUiState.Error(e.message) }
                .collect { result ->
                    _uiState.value = ActivityUiState.Success(
                        notifications = result.notifications,
                        nextCursor = result.nextCursor
                    )
                }
        }
    }

    fun refresh() {
        val currentState = _uiState.value
        if (currentState is ActivityUiState.Success) {
            _uiState.value = currentState.copy(isRefreshing = true)
        }
        viewModelScope.launch {
            notificationsRepository.getNotifications(null)
                .catch { e -> 
                    if (_uiState.value is ActivityUiState.Success) {
                         _uiState.update { (it as ActivityUiState.Success).copy(isRefreshing = false) }
                    } else {
                         _uiState.value = ActivityUiState.Error(e.message)
                    }
                }
                .collect { result ->
                    _uiState.value = ActivityUiState.Success(
                        notifications = result.notifications,
                        nextCursor = result.nextCursor
                    )
                }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState is ActivityUiState.Success && currentState.nextCursor != null && !currentState.isLoadingMore) {
            _uiState.value = currentState.copy(isLoadingMore = true)
            viewModelScope.launch {
                notificationsRepository.getNotifications(currentState.nextCursor)
                    .catch { e -> 
                        _uiState.update { (it as ActivityUiState.Success).copy(isLoadingMore = false) }
                    }
                    .collect { result ->
                        _uiState.update { state ->
                            val successState = state as ActivityUiState.Success
                            successState.copy(
                                notifications = successState.notifications + result.notifications,
                                nextCursor = result.nextCursor,
                                isLoadingMore = false
                            )
                        }
                    }
            }
        }
    }
}
