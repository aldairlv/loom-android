package com.loom.feature.profile.impl

import android.net.Uri
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
import kotlinx.coroutines.flow.update
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
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    // Draft states for editing
    private val _draftDisplayName = MutableStateFlow<String?>(null)
    val draftDisplayName = _draftDisplayName.asStateFlow()

    private val _draftBio = MutableStateFlow<String?>(null)
    val draftBio = _draftBio.asStateFlow()

    private val _draftCity = MutableStateFlow<String?>(null)
    private val _draftTimezone = MutableStateFlow<String?>(null)
    private val _draftCanBeFollowed = MutableStateFlow<Boolean?>(null)
    private val _draftLatitude = MutableStateFlow<Double?>(null)
    private val _draftLongitude = MutableStateFlow<Double?>(null)

    private val _draftAvatar = MutableStateFlow<ByteArray?>(null)
    private val _draftBanner = MutableStateFlow<ByteArray?>(null)

    private val _draftAvatarUri = MutableStateFlow<Uri?>(null)
    val draftAvatarUri = _draftAvatarUri.asStateFlow()

    private val _draftBannerUri = MutableStateFlow<Uri?>(null)
    val draftBannerUri = _draftBannerUri.asStateFlow()

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating = _isUpdating.asStateFlow()

    fun toggleEditMode() {
        _isEditMode.update { 
            val newMode = !it
            if (newMode) {
                // Initialize drafts from current profile
                val currentState = _profileUiState.value
                if (currentState is ProfileUiState.Success) {
                    val p = currentState.profile
                    _draftDisplayName.value = p.displayName
                    _draftBio.value = p.bio
                    _draftCity.value = p.city
                    _draftTimezone.value = p.timezone
                    _draftCanBeFollowed.value = p.canBeFollowed
                    _draftLatitude.value = p.locationCoords?.latitude
                    _draftLongitude.value = p.locationCoords?.longitude
                }
            } else {
                clearDrafts()
            }
            newMode
        }
    }

    fun onDisplayNameChange(newName: String) { _draftDisplayName.value = newName }
    fun onBioChange(newBio: String) { _draftBio.value = newBio }
    fun onCityChange(newCity: String) { _draftCity.value = newCity }
    fun onTimezoneChange(newTimezone: String) { _draftTimezone.value = newTimezone }
    fun onCanBeFollowedChange(canFollow: Boolean) { _draftCanBeFollowed.value = canFollow }
    fun onLocationChange(lat: Double, lon: Double) {
        _draftLatitude.value = lat
        _draftLongitude.value = lon
    }
    fun onAvatarChange(bytes: ByteArray?, uri: Uri?) {
        _draftAvatar.value = bytes
        _draftAvatarUri.value = uri
    }
    fun onBannerChange(bytes: ByteArray?, uri: Uri?) {
        _draftBanner.value = bytes
        _draftBannerUri.value = uri
    }

    fun saveProfileChanges() {
        val currentState = _profileUiState.value
        if (currentState is ProfileUiState.Success) {
            viewModelScope.launch {
                _isUpdating.value = true
                val profile = currentState.profile
                val result = profileRepository.updateProfile(
                    id = profile.id,
                    displayName = _draftDisplayName.value,
                    bio = _draftBio.value,
                    city = _draftCity.value,
                    timezone = _draftTimezone.value,
                    canBeFollowed = _draftCanBeFollowed.value,
                    latitude = _draftLatitude.value,
                    longitude = _draftLongitude.value,
                    avatar = _draftAvatar.value,
                    banner = _draftBanner.value
                )
                when (result) {
                    is Result.Success -> {
                        _profileUiState.value = ProfileUiState.Success(result.data)
                        _isEditMode.value = false
                        clearDrafts()
                    }
                    is Result.Error -> {
                        // Handle error
                    }
                    else -> {}
                }
                _isUpdating.value = false
            }
        }
    }

    fun cancelEdit() {
        _isEditMode.value = false
        clearDrafts()
    }

    private fun clearDrafts() {
        _draftDisplayName.value = null
        _draftBio.value = null
        _draftCity.value = null
        _draftTimezone.value = null
        _draftCanBeFollowed.value = null
        _draftLatitude.value = null
        _draftLongitude.value = null
        _draftAvatar.value = null
        _draftBanner.value = null
        _draftAvatarUri.value = null
        _draftBannerUri.value = null
    }

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
