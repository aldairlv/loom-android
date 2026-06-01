package com.loom.feature.home.impl.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.HomeRepository
import com.loom.core.model.data.FeedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject


sealed interface ForYouUiState {
    data object Loading : ForYouUiState
    data class Success(
        val objects: List<FeedObject>,
        val isFetchingMore: Boolean = false
    ) : ForYouUiState
    data object Error : ForYouUiState
}

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
): ViewModel() {

    private val isFetchingMore = MutableStateFlow(false)
    private val isError = MutableStateFlow(false)

    val uiState: StateFlow<ForYouUiState> = combine(
        homeRepository.getFeedObjectsForYouFlow(),
        isFetchingMore,
        isError
    ) { objects, fetching, error ->
        when {
            error -> ForYouUiState.Error
            objects.isEmpty() && !fetching -> ForYouUiState.Loading // Or a different state for empty
            objects.isEmpty() && fetching -> ForYouUiState.Loading
            else -> ForYouUiState.Success(objects = objects, isFetchingMore = fetching)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ForYouUiState.Loading
    )

    private var currentCursor: String? = null

    init {
        fetchPosts()
    }

    fun fetchPosts(isLoadMore: Boolean = false) {
        if (isLoadMore && currentCursor == null) return

        viewModelScope.launch {
            isFetchingMore.value = true
            isError.value = false

            try {
                // Extract and decode cursor
                val cursorToPass = currentCursor?.let {
                    if (it.contains("cursor=")) {
                        val extracted = it.substringAfter("cursor=").substringBefore("&")
                        URLDecoder.decode(extracted, StandardCharsets.UTF_8.toString())
                    } else {
                        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    }
                }

                val result = homeRepository.getFeedObjectsForYou(
                    cursor = cursorToPass,
                    isRefresh = !isLoadMore
                )

                currentCursor = result.nextCursor
                isFetchingMore.value = false
            } catch (e: Exception) {
                isFetchingMore.value = false
                if (!isLoadMore) {
                    isError.value = true
                }
            }
        }
    }

    fun loadMore() {
        if (!isFetchingMore.value && currentCursor != null) {
            fetchPosts(isLoadMore = true)
        }
    }

    fun onClickLike(postId: String) {
        viewModelScope.launch {
            val currentState = uiState.value
            if (currentState is ForYouUiState.Success) {
                val postObject = currentState.objects.find { it.id == postId } as? FeedObject.PostFeedObject
                val isLiked = postObject?.post?.interactions?.liked ?: false
                try {
                    homeRepository.toggleLike(postId, isLiked)
                } catch (e: Exception) {
                    // Error handled in repository (rollback)
                }
            }
        }
    }

    fun onComment(postId: String) {
        // TODO: Implement comment functionality
    }

    fun onQuickRepost(postId: String) {
        viewModelScope.launch {
            val currentState = uiState.value
            if (currentState is ForYouUiState.Success) {
                val postObject = currentState.objects.find { it.id == postId } as? FeedObject.PostFeedObject
                postObject?.let {
                    val post = it.post
                    try {
                        homeRepository.quickRepost(
                            postId = post.id,
                            parentId = post.id,
                            rootId = post.root?.id ?: post.id
                        )
                    } catch (e: Exception) {
                        // Error handling could be added here
                    }
                }
            }
        }
    }

    fun onCommentRepost(postId: String) {
        // TODO: Implement repost with comment functionality
    }

    fun onShare(postId: String) {
        // TODO: Implement share functionality
    }

    fun onFollowClick(postId: String, authorId: String, isFollowed: Boolean) {
        viewModelScope.launch {
            try {
                homeRepository.toggleFollow(authorId, isFollowed)
            } catch (e: Exception) {
                // Error handled or logged
            }
        }
    }
}
