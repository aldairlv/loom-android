package com.loom.feature.eventeditor.impl

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.EventsRepository
import com.loom.core.data.repository.HomeRepository
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.data.repository.UserRepository
import com.loom.core.model.data.UiEvent
import com.loom.core.ui.event.editor.EventEditorUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

data class MediaUploadStatus(
    val id: String? = null,
    val isUploading: Boolean = false,
)

data class CreateEventUiState(
    override val title: String = "",
    override val thumbnailUri: Uri? = null,
    val assetUris: List<Uri> = emptyList(),
    override val startTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    override val endTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    override val isEndEnabled: Boolean = false,
    override val isDatePickerVisible: Boolean = false,
    // Location Data
    override val locationName: String = "",
    override val locationAddress: String = "",
    override val latitude: Double = 0.0,
    override val longitude: Double = 0.0,
    val timezone: String = TimeZone.currentSystemDefault().id,
    override val isLocationPickerVisible: Boolean = false,
    override val description: String = "",
    override val selectedTags: List<String> = emptyList(),
    val mediaUploads: Map<Uri, MediaUploadStatus> = emptyMap(),
    override val isPublishing: Boolean = false,
) : EventEditorUiState {
    override val allUris: List<Uri> get() = listOfNotNull(thumbnailUri) + assetUris

    override val formattedDateRange: String
        get() {
            val startStr = "${startTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${startTime.dayOfMonth} ${startTime.month.name.lowercase().take(3)} ${startTime.year}"
            return if (isEndEnabled && (startTime.date != endTime.date)) {
                val endStr = "${endTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${endTime.dayOfMonth} ${endTime.month.name.lowercase().take(3)} ${endTime.year}"
                "$startStr - $endStr"
            } else {
                startStr
            }
        }

    override val formattedTimeRange: String
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
    private val homeRepository: HomeRepository,
    private val eventsRepository: EventsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    fun onImagesSelected(context: Context, uris: List<Uri>) {
        _uiState.update { currentState ->
            val newUris = uris.filter { it !in currentState.allUris }
            val totalUris = (currentState.allUris + newUris).distinct()
            val newThumbnail = currentState.thumbnailUri ?: totalUris.firstOrNull()
            val newAssets = totalUris.filter { it != newThumbnail }
            
            val newUploads = currentState.mediaUploads.toMutableMap()
            newUris.forEach { uri ->
                newUploads[uri] = MediaUploadStatus(isUploading = true)
                uploadMediaInternal(context, uri)
            }

            currentState.copy(
                thumbnailUri = newThumbnail,
                assetUris = newAssets,
                mediaUploads = newUploads
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

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun addTag(tag: String) {
        _uiState.update { currentState ->
            if (currentState.selectedTags.size < 50 && !currentState.selectedTags.contains(tag)) {
                currentState.copy(selectedTags = currentState.selectedTags + tag)
            } else {
                currentState
            }
        }
    }

    fun removeTag(tag: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedTags = currentState.selectedTags.filter { it != tag })
        }
    }

    private fun uploadMediaInternal(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val contentResolver = context.contentResolver
                val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return@launch
                val mimeType = contentResolver.getType(uri) ?: "image/jpeg"

                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                val fileName = (uri.lastPathSegment ?: "file").let { name ->
                    if (extension != null && !name.endsWith(".$extension", ignoreCase = true)) {
                        "$name.$extension"
                    } else {
                        name
                    }
                }

                val postMedia = homeRepository.uploadMedia(fileName, mimeType, bytes)

                _uiState.update { currentState ->
                    val newUploads = currentState.mediaUploads.toMutableMap()
                    newUploads[uri] = MediaUploadStatus(id = postMedia.id, isUploading = false)
                    currentState.copy(mediaUploads = newUploads)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.emit(UiEvent.ShowSnackbar("Error al subir archivo"))
                _uiState.update { currentState ->
                    val newUploads = currentState.mediaUploads.toMutableMap()
                    newUploads[uri] = MediaUploadStatus(isUploading = false)
                    currentState.copy(mediaUploads = newUploads)
                }
            }
        }
    }

    fun publicarEvento(onSuccess: () -> Unit) {
        val state = uiState.value

        if (state.title.isBlank()) {
            viewModelScope.launch { _uiEvent.emit(UiEvent.ShowSnackbar("El título es obligatorio")) }
            return
        }

        val isUploading = state.mediaUploads.values.any { it.isUploading }
        if (isUploading) {
            viewModelScope.launch { _uiEvent.emit(UiEvent.ShowSnackbar("Espera a que terminen de subirse las imágenes")) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isPublishing = true) }
            try {
                val assetIds = state.assetUris.mapNotNull { state.mediaUploads[it]?.id }
                val thumbnailId = state.thumbnailUri?.let { state.mediaUploads[it]?.id }

                val startTimeStr = state.startTime.toInstant(TimeZone.of(state.timezone)).toString()
                val endTimeStr = if (state.isEndEnabled) {
                    state.endTime.toInstant(TimeZone.of(state.timezone)).toString()
                } else null

                eventsRepository.createEvent(
                    title = state.title,
                    description = state.description,
                    startTime = startTimeStr,
                    endTime = endTimeStr,
                    locationName = state.locationName.takeIf { it.isNotBlank() },
                    locationAddress = state.locationAddress.takeIf { it.isNotBlank() },
                    assetIds = assetIds,
                    thumbnailId = thumbnailId,
                    latitude = state.latitude,
                    longitude = state.longitude,
                    timezone = state.timezone,
                    tags = state.selectedTags,
                    status = "published"
                )
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.emit(UiEvent.ShowSnackbar("Error al crear el evento"))
            } finally {
                _uiState.update { it.copy(isPublishing = false) }
            }
        }
    }
}
