package com.loom.feature.eventdetail.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.EventsRepository
import com.loom.core.model.data.EventFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val eventsRepository: EventsRepository,
) : ViewModel() {

    private val eventId: String? = savedStateHandle["eventId"]

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        fetchEvent()
    }

    private fun fetchEvent() {
        val id = eventId ?: return
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val event = eventsRepository.getEvent(id)
                if (event != null) {
                    _uiState.value = EventUiState.Success(event)
                } else {
                    _uiState.value = EventUiState.Error("Evento no encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed interface EventUiState {
    data object Loading : EventUiState
    data class Success(val event: EventFeedItem) : EventUiState
    data class Error(val message: String) : EventUiState
}
