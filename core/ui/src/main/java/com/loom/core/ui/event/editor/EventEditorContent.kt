package com.loom.core.ui.event.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime

@Composable
fun EventEditorContent(
    modifier: Modifier = Modifier,
    uiState: EventEditorUiState, // We need to define this or use a generic one
    snackbarHostState: SnackbarHostState,
    onClose: () -> Unit,
    onImagesSelected: (List<Uri>) -> Unit,
    onThumbnailSelected: (Uri) -> Unit,
    onTitleChange: (String) -> Unit,
    onToggleDatePicker: () -> Unit,
    onToggleEndEnabled: (Boolean) -> Unit,
    onUpdateStartTime: (LocalDateTime) -> Unit,
    onUpdateEndTime: (LocalDateTime) -> Unit,
    onCancelDate: () -> Unit,
    onSaveDate: () -> Unit,
    onToggleLocationPicker: () -> Unit,
    onLocationNameChange: (String) -> Unit,
    onLocationSelected: (Double, Double, String) -> Unit,
    onCancelLocation: () -> Unit,
    onSaveLocation: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    onCreateEvent: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> if (uris.isNotEmpty()) onImagesSelected(uris) }
    )

    val listState = rememberLazyListState()
    var showTagsBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isDatePickerVisible) {
        if (uiState.isDatePickerVisible) {
            delay(300)
            listState.animateScrollToItem(index = 5)
        }
    }

    LaunchedEffect(uiState.isLocationPickerVisible) {
        if (uiState.isLocationPickerVisible) {
            delay(300)
            listState.animateScrollToItem(index = 7)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EventEditorTopBar(
                onClose = onClose,
                onCreateClick = onCreateEvent,
                isPublishing = uiState.isPublishing
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            userScrollEnabled = !uiState.isDatePickerVisible && !uiState.isLocationPickerVisible
        ) {
            item {
                PhotoArea(
                    uris = uiState.allUris,
                    onAddPhotosClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            item {
                TitleInput(
                    title = uiState.title,
                    onTitleChange = onTitleChange
                )
            }

            item {
                DescriptionInput(
                    description = uiState.description,
                    onDescriptionChange = onDescriptionChange
                )
            }

            item {
                TagSelectionRow(
                    selectedTags = uiState.selectedTags,
                    onAddTagsClick = { showTagsBottomSheet = true }
                )
            }

            item {
                DateInfoSection(
                    formattedDateRange = uiState.formattedDateRange,
                    formattedTimeRange = uiState.formattedTimeRange,
                    onEditClick = onToggleDatePicker
                )
            }

            item {
                AnimatedVisibility(visible = uiState.isDatePickerVisible) {
                    DateEditorSection(
                        startTime = uiState.startTime,
                        endTime = uiState.endTime,
                        isEndEnabled = uiState.isEndEnabled,
                        onUpdateStartTime = onUpdateStartTime,
                        onUpdateEndTime = onUpdateEndTime,
                        onToggleEndEnabled = onToggleEndEnabled,
                        onCancel = onCancelDate,
                        onSave = onSaveDate
                    )
                }
            }

            item {
                LocationInfoSection(
                    locationName = uiState.locationName.ifBlank { "Location Name" },
                    locationAddress = uiState.locationAddress.ifBlank { "Location Address" },
                    onEditClick = onToggleLocationPicker
                )
            }

            item {
                AnimatedVisibility(visible = uiState.isLocationPickerVisible) {
                    LocationEditorSection(
                        locationName = uiState.locationName,
                        locationAddress = uiState.locationAddress,
                        latitude = uiState.latitude,
                        longitude = uiState.longitude,
                        onLocationNameChange = onLocationNameChange,
                        onLocationSelected = onLocationSelected,
                        onCancel = onCancelLocation,
                        onSave = onSaveLocation
                    )
                }
            }

            if (uiState.allUris.isNotEmpty()) {
                item {
                    ThumbnailSelectorSection(
                        allUris = uiState.allUris,
                        thumbnailUri = uiState.thumbnailUri,
                        onThumbnailSelected = onThumbnailSelected
                    )
                }
            }
        }
    }

    if (showTagsBottomSheet) {
        EventTagsBottomSheet(
            selectedTags = uiState.selectedTags,
            onAddTag = onAddTag,
            onRemoveTag = onRemoveTag,
            onDismiss = { showTagsBottomSheet = false }
        )
    }
}

// Interface to be implemented by the feature's UiState
interface EventEditorUiState {
    val isPublishing: Boolean
    val isDatePickerVisible: Boolean
    val isLocationPickerVisible: Boolean
    val allUris: List<Uri>
    val title: String
    val description: String
    val selectedTags: List<String>
    val formattedDateRange: String
    val formattedTimeRange: String
    val startTime: LocalDateTime
    val endTime: LocalDateTime
    val isEndEnabled: Boolean
    val locationName: String
    val locationAddress: String
    val latitude: Double
    val longitude: Double
    val thumbnailUri: Uri?
}
