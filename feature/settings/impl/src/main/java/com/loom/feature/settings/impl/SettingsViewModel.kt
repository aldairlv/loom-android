package com.loom.feature.settings.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.common.result.Result
import com.loom.core.data.repository.AuthRepository
import com.loom.core.data.repository.ProfileRepository
import com.loom.core.data.repository.SettingsRepository
import com.loom.core.model.data.UserAccountProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            when (val result = profileRepository.getMyProfile()) {
                is Result.Success -> {
                    _uiState.update { it.copy(profile = result.data) }
                }
                else -> {
                    // Handle error
                }
            }
        }
    }

    fun onAllowQuestionsChanged(allow: Boolean) {
        _uiState.update { it.copy(allowQuestions = allow) }
    }

    fun onAllowAnonymousQuestionsChanged(allow: Boolean) {
        _uiState.update { it.copy(allowAnonymousQuestions = allow) }
    }

    fun onAllowMultimediaQuestionsChanged(allow: Boolean) {
        _uiState.update { it.copy(allowMultimediaQuestions = allow) }
    }

    fun onShowPopularPostsChanged(show: Boolean) {
        _uiState.update { it.copy(showPopularPosts = show) }
    }

    fun onOptimizeVideosChanged(optimize: Boolean) {
        _uiState.update { it.copy(optimizeVideos = optimize) }
    }

    fun onDisableDoubleTapLikeChanged(disable: Boolean) {
        _uiState.update { it.copy(disableDoubleTapLike = disable) }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

data class SettingsUiState(
    val profile: UserAccountProfile? = null,
    val allowQuestions: Boolean = true,
    val allowAnonymousQuestions: Boolean = true,
    val allowMultimediaQuestions: Boolean = true,
    val showPopularPosts: Boolean = true,
    val optimizeVideos: Boolean = true,
    val disableDoubleTapLike: Boolean = false,
)
