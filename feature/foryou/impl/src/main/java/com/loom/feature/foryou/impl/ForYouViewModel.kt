package com.loom.feature.foryou.impl


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.ui.PostFeedUiState
//import com.loom.core.data.repository.PostRepository
import com.loom.core.data.repository.TimelineRepository
import com.loom.core.model.data.TimelineObject
import com.loom.core.model.enum.TimelineCategory
//import com.loom.core.ui.TimelineUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    //private val postRepository: PostRepository,
    private val timelineRepository: TimelineRepository,
) : ViewModel() {
    /*val timelineState: StateFlow<TimelineUiState> =
        timelineRepository.getObjects(TimelineCategory.FOR_YOU)
            .map<List<TimelineObject>, TimelineUiState>(TimelineUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TimelineUiState.Loading,
            )*/

    fun updateLike(postId: String, isLiked: Boolean) {
        viewModelScope.launch {
            // postRepository.updateLike(postId, isLiked)
        }
    }
    private var isLoadingMore = false

    fun loadMore() {
        if (isLoadingMore) return

        viewModelScope.launch {
            isLoadingMore = true
            timelineRepository.syncTimeline(
                timelineCategory = TimelineCategory.FOR_YOU,
                forceRefresh = false
            )
            isLoadingMore = false
        }
    }
}