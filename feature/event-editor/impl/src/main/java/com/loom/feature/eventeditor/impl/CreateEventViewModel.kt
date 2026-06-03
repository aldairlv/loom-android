package com.loom.feature.eventeditor.impl

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CreateEventUiState(
    val title: String = "",
    val thumbnailUri: Uri? = null,
    val assetUris: List<Uri> = emptyList(),
) {
    val allUris: List<Uri> get() = listOfNotNull(thumbnailUri) + assetUris
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
}
