package com.loom.core.ui


import com.loom.core.model.data.Post

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.items

/**
 * Extensión para [LazyStaggeredGridScope] que define un feed de posts.
 * Siguiendo el patrón de NiA, esta función decide qué renderizar según el [feedState].
 */
fun LazyStaggeredGridScope.postsFeed(
    feedState: PostFeedUiState,
    onPostClick: (String) -> Unit,
) {
    when (feedState) {
        PostFeedUiState.Loading -> {
            // Aquí se podria añadir items de "esqueleto" (shimmer)
        }
        is PostFeedUiState.Success -> {
            items(
                items = feedState.feed,
                key = { it.id }, // Clave única para optimizar el scroll
                contentType = { "postItem" }, // Ayuda a Compose a reciclar la UI
            ) { post ->
                // Aquí llamaremos a PostCard (que crearemos después)
                // Por ahora, un placeholder:
                PostCardExpanded(
                    post = post,
                    //onClick = { onPostClick(post.id) }
                )
            }
        }
    }
}






/**
 * Interfaz sellada que describe el estado de la lista de posts.
 */
sealed interface PostFeedUiState {
    /**
     * El feed se está cargando.
     */
    data object Loading : PostFeedUiState

    /**
     * El feed se ha cargado con éxito.
     */
    data class Success(
        /**
         * La lista de posts que se mostrarán.
         */
        val feed: List<Post>,
    ) : PostFeedUiState
}