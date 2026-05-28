package com.loom.feature.profile.impl.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.common.result.Result
import com.loom.core.data.repository.ProfileRepository
import com.loom.core.model.data.PostFeedItem
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

sealed interface LikesUiState {
    data object Loading : LikesUiState
    data class Success(
        val posts: List<PostFeedItem>,
        val isFetchingMore: Boolean = false,
        val isEmpty: Boolean = false
    ) : LikesUiState
    data class Error(val message: String? = null) : LikesUiState
}

@HiltViewModel
class LikesViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<LikesUiState>(LikesUiState.Loading)
    val uiState: StateFlow<LikesUiState> = _uiState.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>()
    val snackbarMessages: SharedFlow<String> = _snackbarMessages.asSharedFlow()

    private var currentCursor: String? = null
    private val allPosts = mutableListOf<PostFeedItem>()

    init {
        fetchPosts()
    }

    fun fetchPosts(isLoadMore: Boolean = false) {
        if (isLoadMore && currentCursor == null) return

        viewModelScope.launch {
            if (!isLoadMore) {
                _uiState.value = LikesUiState.Loading
                allPosts.clear()
                currentCursor = null
            } else {
                val current = _uiState.value
                if (current is LikesUiState.Success) {
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

            when (val result = profileRepository.getLikedPosts(cursorToPass)) {
                is Result.Success -> {
                    val data = result.data
                    currentCursor = data.nextCursor

                    // Avoid duplicates just in case
                    val newPosts =
                        data.posts.filter { newPost -> allPosts.none { it.id == newPost.id } }
                    allPosts.addAll(newPosts)

                    _uiState.value = LikesUiState.Success(
                        posts = allPosts.toList(),
                        isFetchingMore = false,
                        isEmpty = allPosts.isEmpty()
                    )
                }

                is Result.Error -> {
                    val errorMessage = result.exception.message ?: "Unknown error"
                    _snackbarMessages.emit(errorMessage)
                    _uiState.value = LikesUiState.Error(errorMessage)
                }

                is Result.Loading -> {
                    // Handled by local state
                }
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (state is LikesUiState.Success && !state.isFetchingMore && currentCursor != null) {
            fetchPosts(isLoadMore = true)
        }
    }

    fun onClickLike(postId: String) {
        // TODO: Implement like functionality
    }

    fun onComment(postId: String) {
        // TODO: Implement comment functionality
    }

    fun onRepost(postId: String) {
        // TODO: Implement repost functionality
    }

    fun onShare(postId: String) {
        // TODO: Implement share functionality
    }
}
