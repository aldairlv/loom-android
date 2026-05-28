package com.loom.feature.profile.impl.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.common.result.Result
import com.loom.core.data.repository.ProfileRepository
import com.loom.core.model.data.FollowingUserFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

sealed interface FollowingUsersUiState {
    data object Loading : FollowingUsersUiState
    data class Success(
        val followingUsers: List<FollowingUserFeedItem>,
        val isFetchingMore: Boolean = false,
        val isEmpty: Boolean = false
    ) : FollowingUsersUiState
    data class Error(val message: String? = null) : FollowingUsersUiState
}

@HiltViewModel
class FollowingUsersViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<FollowingUsersUiState>(FollowingUsersUiState.Loading)
    val uiState: StateFlow<FollowingUsersUiState> = _uiState.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>()
    val snackbarMessages: SharedFlow<String> = _snackbarMessages.asSharedFlow()

    private var currentCursor: String? = null
    private val allUsers = mutableListOf<FollowingUserFeedItem>()

    init {
        fetchUsers()
    }

    fun fetchUsers(isLoadMore: Boolean = false) {
        if (isLoadMore && currentCursor == null) return

        viewModelScope.launch {
            if (!isLoadMore) {
                _uiState.value = FollowingUsersUiState.Loading
                allUsers.clear()
                currentCursor = null
            } else {
                val current = _uiState.value
                if (current is FollowingUsersUiState.Success) {
                    _uiState.value = current.copy(isFetchingMore = true)
                }
            }

            // Extract and decode cursor
            val cursorToPass = currentCursor?.let {
                if (it.contains("cursor=")) {
                    val extracted = it.substringAfter("cursor=").substringBefore("&")
                    URLDecoder.decode(extracted, StandardCharsets.UTF_8.toString())
                } else {
                    URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                }
            }

            when (val result = profileRepository.getFollowingUsers(cursorToPass)) {
                is Result.Success -> {
                    val data = result.data
                    currentCursor = data.nextCursor

                    // Avoid duplicates just in case
                    val newUsers =
                        data.users.filter { newUser -> allUsers.none { it.id == newUser.id } }
                    allUsers.addAll(newUsers)

                    _uiState.value = FollowingUsersUiState.Success(
                        followingUsers = allUsers.toList(),
                        isFetchingMore = false,
                        isEmpty = allUsers.isEmpty()
                    )
                }

                is Result.Error -> {
                    val errorMessage = result.exception.message ?: "Unknown error"
                    _snackbarMessages.emit(errorMessage)
                    _uiState.value = FollowingUsersUiState.Error(errorMessage)
                }

                is Result.Loading -> {
                    // Handled by local state
                }
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (state is FollowingUsersUiState.Success && !state.isFetchingMore && currentCursor != null) {
            fetchUsers(isLoadMore = true)
        }
    }

    fun onUnfollow(userId: String) {
        // TODO: Implement unfollow functionality
    }

    fun onBlock(userId: String) {
        // TODO: Implement block functionality
    }
}
