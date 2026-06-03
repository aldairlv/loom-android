package com.loom.feature.eventeditor.impl

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

data class CreateEventUiState(
    val title: String = "",
    val thumbnailUri: Uri? = null,
    val assetUris: List<Uri> = emptyList(),
    val startTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val endTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val isEndEnabled: Boolean = false,
    val isDatePickerVisible: Boolean = false,
    // Location Data
    val locationName: String = "",
    val locationAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = TimeZone.currentSystemDefault().id,
    val isLocationPickerVisible: Boolean = false,
) {
    val allUris: List<Uri> get() = listOfNotNull(thumbnailUri) + assetUris

    val formattedDateRange: String
        get() {
            val startStr = "${startTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${startTime.dayOfMonth} ${startTime.month.name.lowercase().take(3)} ${startTime.year}"
            return if (isEndEnabled && (startTime.date != endTime.date)) {
                val endStr = "${endTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${endTime.dayOfMonth} ${endTime.month.name.lowercase().take(3)} ${endTime.year}"
                "$startStr - $endStr"
            } else {
                startStr
            }
        }

    val formattedTimeRange: String
        get() {
            val timeZone = TimeZone.currentSystemDefault()
            val startStr = String.format("%02d:%02d", startTime.hour, startTime.minute)
            val zoneStr = "GMT${timeZone.id}" // Simplified
            
            return if (isEndEnabled) {
                val endStr = String.format("%02d:%02d", endTime.hour, endTime.minute)
                if (startTime.hour == endTime.hour && startTime.minute == endTime.minute && startTime.date == endTime.date) {
                    "$startStr $zoneStr"
                } else {
                    "$startStr - $endStr $zoneStr"
                }
            } else {
                "$startStr $zoneStr"
            }
        }
}

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState = _uiState.asStateFlow()

    fun onImagesSelected(uris: List<Uri>) {
        _uiState.update { currentState ->
            val totalUris = (currentState.allUris + uris).distinct()
            val newThumbnail = currentState.thumbnailUri ?: totalUris.firstOrNull()
            val newAssets = totalUris.filter { it != newThumbnail }
            
            currentState.copy(
                thumbnailUri = newThumbnail,
                assetUris = newAssets
            )
        }
    }

    fun onThumbnailSelected(uri: Uri) {
        _uiState.update { currentState ->
            val totalUris = currentState.allUris
            if (uri == currentState.thumbnailUri) return@update currentState
            
            currentState.copy(
                thumbnailUri = uri,
                assetUris = totalUris.filter { it != uri }
            )
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun toggleDatePicker() {
        _uiState.update { it.copy(isDatePickerVisible = !it.isDatePickerVisible) }
    }

    fun onToggleEndEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isEndEnabled = enabled) }
    }

    fun updateStartTime(newDateTime: LocalDateTime) {
        _uiState.update { it.copy(startTime = newDateTime) }
    }

    fun updateEndTime(newDateTime: LocalDateTime) {
        _uiState.update { it.copy(endTime = newDateTime) }
    }

    fun cancelDateSelection() {
        _uiState.update { it.copy(isDatePickerVisible = false) }
    }

    fun saveDateSelection() {
        _uiState.update { it.copy(isDatePickerVisible = false) }
    }

    // Location Actions
    fun toggleLocationPicker() {
        _uiState.update { it.copy(isLocationPickerVisible = !it.isLocationPickerVisible) }
    }

    fun onLocationNameChange(name: String) {
        _uiState.update { it.copy(locationName = name) }
    }

    fun onLocationSelected(latitude: Double, longitude: Double, address: String) {
        _uiState.update { 
            it.copy(
                latitude = latitude,
                longitude = longitude,
                locationAddress = address
            )
        }
    }

    fun cancelLocationSelection() {
        _uiState.update { it.copy(isLocationPickerVisible = false) }
    }

    fun saveLocationSelection() {
        _uiState.update { it.copy(isLocationPickerVisible = false) }
    }
}
