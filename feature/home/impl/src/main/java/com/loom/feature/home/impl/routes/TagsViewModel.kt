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


sealed interface TagsUiState {
    data object Loading : TagsUiState
    data class Success(
        val posts: List<PostFeedItem>,
        val isFetchingMore: Boolean = false
    ) : TagsUiState
    data object Error : TagsUiState
}

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow<TagsUiState>(TagsUiState.Loading)
    val uiState: StateFlow<TagsUiState> = _uiState.asStateFlow()

    private var currentCursor: String? = null
    private val allPosts = mutableListOf<PostFeedItem>()

    init {
        fetchPosts()
    }

    fun fetchPosts(isLoadMore: Boolean = false) {
        if (isLoadMore && currentCursor == null) return

        viewModelScope.launch {
            if (!isLoadMore) {
                _uiState.value = TagsUiState.Loading
                allPosts.clear()
                currentCursor = null
            } else {
                val current = _uiState.value
                if (current is TagsUiState.Success) {
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

                val result = homeRepository.getPostsFeedTags(cursorToPass)

                currentCursor = result.nextCursor

                val newPosts = result.posts.filter { newPost -> allPosts.none { it.id == newPost.id } }
                allPosts.addAll(newPosts)

                _uiState.value = TagsUiState.Success(
                    posts = allPosts.toList(),
                    isFetchingMore = false
                )
            } catch (e: Exception) {
                _uiState.value = TagsUiState.Error
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (state is TagsUiState.Success && !state.isFetchingMore && currentCursor != null) {
            fetchPosts(isLoadMore = true)
        }
    }

    fun onClickLike(postId: String) {
        viewModelScope.launch {
            val currentState = uiState.value
            if (currentState is TagsUiState.Success) {
                val post = currentState.posts.find { it.id == postId }
                val isLiked = post?.interactions?.liked ?: false
                try {
                    homeRepository.toggleLike(postId, isLiked)
                } catch (e: Exception) {
                    // Error handled in repository (rollback)
                }
            }
        }
    }

    fun onComment(postId: String) {
        // TODO
    }

    fun onQuickRepost(postId: String) {
        viewModelScope.launch {
            val currentState = uiState.value
            if (currentState is TagsUiState.Success) {
                val post = currentState.posts.find { it.id == postId }
                post?.let {
                    try {
                        homeRepository.quickRepost(
                            postId = it.id,
                            parentId = it.id,
                            rootId = it.root?.id ?: it.id
                        )
                    } catch (e: Exception) {
                        // Error handling
                    }
                }
            }
        }
    }

    fun onCommentRepost(postId: String) {
        // TODO
    }

    fun onShare(postId: String) {
        // TODO
    }
}
