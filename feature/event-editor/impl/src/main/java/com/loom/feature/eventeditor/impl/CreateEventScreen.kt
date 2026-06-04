package com.loom.feature.eventeditor.impl

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.UiEvent
import com.loom.core.ui.event.editor.EventEditorContent

@Composable
fun EventEditorScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    viewModel: CreateEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    EventEditorContent(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onClose = onClose,
        onImagesSelected = { uris -> viewModel.onImagesSelected(context, uris) },
        onThumbnailSelected = viewModel::onThumbnailSelected,
        onTitleChange = viewModel::onTitleChange,
        onToggleDatePicker = viewModel::toggleDatePicker,
        onToggleEndEnabled = viewModel::onToggleEndEnabled,
        onUpdateStartTime = viewModel::updateStartTime,
        onUpdateEndTime = viewModel::updateEndTime,
        onCancelDate = viewModel::cancelDateSelection,
        onSaveDate = viewModel::saveDateSelection,
        // Location callbacks
        onToggleLocationPicker = viewModel::toggleLocationPicker,
        onLocationNameChange = viewModel::onLocationNameChange,
        onLocationSelected = viewModel::onLocationSelected,
        onCancelLocation = viewModel::cancelLocationSelection,
        onSaveLocation = viewModel::saveLocationSelection,
        onDescriptionChange = viewModel::onDescriptionChange,
        onAddTag = viewModel::addTag,
        onRemoveTag = viewModel::removeTag,
        onCreateEvent = {
            viewModel.publicarEvento(onSuccess = onClose)
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun EventEditorScreenPreview() {
    LoomTheme {
        EventEditorScreen(
            onClose = {}
        )
    }
}
