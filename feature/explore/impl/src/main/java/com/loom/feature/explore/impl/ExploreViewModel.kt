package com.loom.feature.explore.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.loom.core.data.repository.ExploreRepository
//import com.loom.core.model.data.TimelineObject
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
class ExploreViewModel @Inject constructor(
    //private val exploreRepository: ExploreRepository,
): ViewModel() {
    /*val timelineState: StateFlow<TimelineUiState> =
        exploreRepository.getObjects(TimelineCategory.EXPLORE)
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
}