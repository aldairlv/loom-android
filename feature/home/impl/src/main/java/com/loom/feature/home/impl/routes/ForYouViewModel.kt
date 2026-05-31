package com.loom.feature.home.impl.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.HomeRepository
import com.loom.core.model.data.FeedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _uiState = MutableStateFlow<ForYouUiState>(ForYouUiState.Loading)
    val uiState: StateFlow<ForYouUiState> = _uiState.asStateFlow()

    private var currentCursor: String? = null
    private val allObjects = mutableListOf<FeedObject>()

    init {
        fetchPosts()
    }

    fun fetchPosts(isLoadMore: Boolean = false) {
        if (isLoadMore && currentCursor == null) return

        viewModelScope.launch {
            if (!isLoadMore) {
                _uiState.value = ForYouUiState.Loading
                allObjects.clear()
                currentCursor = null
            } else {
                val current = _uiState.value
                if (current is ForYouUiState.Success) {
                    _uiState.value = current.copy(isFetchingMore = true)
                }
            }

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

                val result = homeRepository.getFeedObjectsForYou(cursorToPass)

                currentCursor = result.nextCursor

                // Avoid duplicates just in case
                val newObjects = result.objects.filter { newObj -> allObjects.none { it.id == newObj.id } }
                allObjects.addAll(newObjects)

                _uiState.value = ForYouUiState.Success(
                    objects = allObjects.toList(),
                    isFetchingMore = false
                )
            } catch (e: Exception) {
                _uiState.value = ForYouUiState.Error
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (state is ForYouUiState.Success && !state.isFetchingMore && currentCursor != null) {
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
