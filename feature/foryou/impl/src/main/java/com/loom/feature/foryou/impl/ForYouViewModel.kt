package com.loom.feature.foryou.impl


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.ui.PostFeedUiState // Verifica que esta sea la ruta correcta de tu UI State
import com.loom.core.data.repository.PostRepository
import com.loom.core.model.data.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val postRepository: PostRepository, // Tu repositorio de posts
) : ViewModel() {

    // Transformamos el flujo de datos del repositorio en un StateFlow para la UI
    val feedState: StateFlow<PostFeedUiState> =
        postRepository.getPosts()
            .map<List<Post>, PostFeedUiState>(PostFeedUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PostFeedUiState.Loading,
            )

    fun updateLike(postId: String, isLiked: Boolean) {
        viewModelScope.launch {
            // postRepository.updateLike(postId, isLiked)
        }
    }
}