package com.loom.feature.comments.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.HomeRepository
import com.loom.core.model.data.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CommentsUiState {
    data object Loading : CommentsUiState
    data class Success(
        val comments: List<Comment>,
        val isFetchingMore: Boolean = false
    ) : CommentsUiState
    data object Error : CommentsUiState
}

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private var postId: String? = null

    private val _uiState = MutableStateFlow<CommentsUiState>(CommentsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentCursor: String? = null
    private var isFetchingMore = false

    fun setPostId(id: String) {
        if (postId == null) {
            postId = id
            fetchComments()
        }
    }

    fun fetchComments(isLoadMore: Boolean = false) {
        val id = postId ?: return
        if (isLoadMore && currentCursor == null) return
        if (isFetchingMore) return

        viewModelScope.launch {
            isFetchingMore = true
            if (!isLoadMore) {
                _uiState.value = CommentsUiState.Loading
            } else {
                val currentState = _uiState.value
                if (currentState is CommentsUiState.Success) {
                    _uiState.value = currentState.copy(isFetchingMore = true)
                }
            }

            try {
                val result = homeRepository.getComments(id, currentCursor)
                currentCursor = result.nextCursor
                
                val currentComments = (_uiState.value as? CommentsUiState.Success)?.comments ?: emptyList()
                val newComments = if (isLoadMore) currentComments + result.comments else result.comments
                
                _uiState.value = CommentsUiState.Success(
                    comments = newComments,
                    isFetchingMore = false
                )
            } catch (e: Exception) {
                _uiState.value = CommentsUiState.Error
            } finally {
                isFetchingMore = false
            }
        }
    }

    fun sendComment(text: String, parentId: String? = null) {
        val id = postId ?: return
        viewModelScope.launch {
            try {
                if (parentId == null) {
                    homeRepository.createComment(id, text)
                } else {
                    homeRepository.createReply(id, parentId, text)
                }
                // Refresh comments
                currentCursor = null
                fetchComments(isLoadMore = false)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
