package com.loom.feature.notifications.impl

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

sealed interface NotificationsUiState {
    object Loading : NotificationsUiState
    data class Success(
        val notifications: List<Notification>,
        val isRefreshing: Boolean = false,
        val isLoadingMore: Boolean = false,
        val nextCursor: String? = null
    ) : NotificationsUiState
    data class Error(val message: String?) : NotificationsUiState
}

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationsUiState>(NotificationsUiState.Loading)
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationsUiState.Loading
            notificationsRepository.getNotifications(null)
                .catch { e -> _uiState.value = NotificationsUiState.Error(e.message) }
                .collect { result ->
                    _uiState.value = NotificationsUiState.Success(
                        notifications = result.notifications,
                        nextCursor = result.nextCursor
                    )
                }
        }
    }

    fun refresh() {
        val currentState = _uiState.value
        if (currentState is NotificationsUiState.Success) {
            _uiState.value = currentState.copy(isRefreshing = true)
        }
        viewModelScope.launch {
            notificationsRepository.getNotifications(null)
                .catch { e -> 
                    if (_uiState.value is NotificationsUiState.Success) {
                         _uiState.update { (it as NotificationsUiState.Success).copy(isRefreshing = false) }
                    } else {
                         _uiState.value = NotificationsUiState.Error(e.message)
                    }
                }
                .collect { result ->
                    _uiState.value = NotificationsUiState.Success(
                        notifications = result.notifications,
                        nextCursor = result.nextCursor
                    )
                }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState is NotificationsUiState.Success && currentState.nextCursor != null && !currentState.isLoadingMore) {
            _uiState.value = currentState.copy(isLoadingMore = true)
            viewModelScope.launch {
                notificationsRepository.getNotifications(currentState.nextCursor)
                    .catch { e -> 
                        _uiState.update { (it as NotificationsUiState.Success).copy(isLoadingMore = false) }
                    }
                    .collect { result ->
                        _uiState.update { state ->
                            val successState = state as NotificationsUiState.Success
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
