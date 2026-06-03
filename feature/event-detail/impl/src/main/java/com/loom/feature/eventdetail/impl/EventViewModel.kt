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
        val id = eventId ?: run {
            android.util.Log.e("LOOM_EVENT_DETAIL", "ViewModel: eventId is null")
            return
        }
        android.util.Log.d("LOOM_EVENT_DETAIL", "ViewModel: Fetching event with id: $id")
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val event = eventsRepository.getEvent(id)
                if (event != null) {
                    android.util.Log.d("LOOM_EVENT_DETAIL", "ViewModel: Event successfully loaded: ${event.id}")
                    _uiState.value = EventUiState.Success(event)
                } else {
                    android.util.Log.e("LOOM_EVENT_DETAIL", "ViewModel: Event not found for id: $id")
                    _uiState.value = EventUiState.Error("Evento no encontrado")
                }
            } catch (e: Exception) {
                android.util.Log.e("LOOM_EVENT_DETAIL", "ViewModel: Error fetching event", e)
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
