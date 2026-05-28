package com.loom.feature.profile.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.common.result.Result
import com.loom.core.data.repository.ProfileRepository
import com.loom.core.model.data.UserAccountProfile
import com.loom.core.ui.tabs.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val profile: UserAccountProfile) : ProfileUiState
    data class Error(val message: String?) : ProfileUiState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _tabs = MutableStateFlow(
        listOf(
            ProfileTab.Posts.toTabItem(enabled = true),
            ProfileTab.Likes.toTabItem(enabled = true),
            ProfileTab.FollowingUsers.toTabItem(enabled = true)
        )
    )
    val tabs: StateFlow<List<TabItem>> = _tabs.asStateFlow()

    private val _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _profileUiState.value = ProfileUiState.Loading
            when (val result = profileRepository.getMyProfile()) {
                is Result.Success -> {
                    _profileUiState.value = ProfileUiState.Success(result.data)
                }
                is Result.Error -> {
                    _profileUiState.value = ProfileUiState.Error(result.exception.message)
                }
                is Result.Loading -> {
                    // Already set to loading
                }
            }
        }
    }
}
