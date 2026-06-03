package com.loom.feature.events.impl.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.EventsRepository
import com.loom.core.model.data.FeedObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

sealed interface SoonUiState {
    data object Loading : SoonUiState
    data class Success(
        val objects: List<FeedObject>,
        val isFetchingMore: Boolean = false
    ) : SoonUiState
    data object Error : SoonUiState
}

@HiltViewModel
class SoonViewModel @Inject constructor(
    private val eventsRepository: com.loom.core.data.repository.EventsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SoonUiState>(SoonUiState.Loading)
    val uiState: StateFlow<SoonUiState> = _uiState.asStateFlow()

    private val isFetchingMore = MutableStateFlow(false)
    private var currentCursor: String? = null
    private var allObjects = mutableListOf<FeedObject>()

    init {
        fetchSoonEvents()
    }

    fun fetchSoonEvents(isLoadMore: Boolean = false) {
        if (isLoadMore && currentCursor == null) return

        viewModelScope.launch {
            if (isLoadMore || allObjects.isNotEmpty()) {
                isFetchingMore.value = true
                if (uiState.value is SoonUiState.Success) {
                    _uiState.value = (uiState.value as SoonUiState.Success).copy(isFetchingMore = true)
                }
            } else {
                _uiState.value = SoonUiState.Loading
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

                val result = eventsRepository.getFeedEventsSoon(
                    cursor = cursorToPass,
                    isRefresh = !isLoadMore
                )

                if (isLoadMore) {
                    allObjects.addAll(result.objects)
                } else {
                    allObjects = result.objects.toMutableList()
                }

                currentCursor = result.nextCursor
                _uiState.value = SoonUiState.Success(
                    objects = allObjects,
                    isFetchingMore = false
                )
                isFetchingMore.value = false
            } catch (e: Exception) {
                isFetchingMore.value = false
                if (!isLoadMore) {
                    _uiState.value = SoonUiState.Error
                }
            }
        }
    }

    fun loadMore() {
        if (!isFetchingMore.value && currentCursor != null) {
            fetchSoonEvents(isLoadMore = true)
        }
    }

    fun onClickLike(postId: String) {
        // Implement when likes endpoint for events is ready
    }

    fun onQuickRepost(postId: String) {}

    fun onShare(postId: String) {}

    fun onFollowClick(postId: String, authorId: String, isFollowed: Boolean) {}
}
