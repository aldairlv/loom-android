package com.loom.feature.home.impl.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.HomeRepository
import com.loom.core.model.data.PostFeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


sealed interface FollowingUiState {
    data object Loading : FollowingUiState
    data class Success(
        val posts: List<PostFeedItem>,
        val isFetchingMore: Boolean = false
    ) : FollowingUiState
    data object Error : FollowingUiState
}

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow<FollowingUiState>(FollowingUiState.Loading)
    val uiState: StateFlow<FollowingUiState> = _uiState.asStateFlow()

    private var currentCursor: String? = null
    private val allPosts = mutableListOf<PostFeedItem>()

    init {
        fetchPosts()
    }

    fun fetchPosts(isLoadMore: Boolean = false) {
        if (isLoadMore && currentCursor == null) return

        viewModelScope.launch {
            if (!isLoadMore) {
                _uiState.value = FollowingUiState.Loading
                allPosts.clear()
                currentCursor = null
            } else {
                val current = _uiState.value
                if (current is FollowingUiState.Success) {
                    _uiState.value = current.copy(isFetchingMore = true)
                }
            }

            try {
                val cursorToPass = currentCursor?.let {
                    if (it.contains("cursor=")) {
                        val extracted = it.substringAfter("cursor=").substringBefore("&")
                        URLDecoder.decode(extracted, StandardCharsets.UTF_8.toString())
                    } else {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    }
                }

                val result = homeRepository.getPostsFeedFollowing(cursorToPass)

                currentCursor = result.nextCursor

                val newPosts = result.posts.filter { newPost -> allPosts.none { it.id == newPost.id } }
                allPosts.addAll(newPosts)

                _uiState.value = FollowingUiState.Success(
                    posts = allPosts.toList(),
                    isFetchingMore = false
                )
            } catch (e: Exception) {
                _uiState.value = FollowingUiState.Error
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (state is FollowingUiState.Success && !state.isFetchingMore && currentCursor != null) {
            fetchPosts(isLoadMore = true)
        }
    }

    fun onClickLike(postId: String) {
        // TODO
    }

    fun onComment(postId: String) {
        // TODO
    }

    fun onRepost(postId: String) {
        // TODO
    }

    fun onShare(postId: String) {
        // TODO
    }
}
