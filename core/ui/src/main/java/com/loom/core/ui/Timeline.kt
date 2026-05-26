package com.loom.core.ui
//import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
//import androidx.compose.foundation.lazy.staggeredgrid.items

/*

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.Post
import com.loom.core.model.data.TimelineObject


fun LazyListScope.timeline(
    timelineState: TimelineUiState,
    onPostClick: (String) -> Unit,
) {
    when (timelineState) {
        TimelineUiState.Loading -> {
            // Aquí se podria añadir items de "esqueleto" (shimmer)
        }
        is TimelineUiState.Success -> {
            items(
                items = timelineState.timelineObjects,
                key = { it.id }, // Clave única para optimizar el scroll
                contentType = { "objectItem" }, // Ayuda a Compose a reciclar la UI
            ) { timelineObject ->
                // Llamamos a una función que se encarga de repartir el trabajo
                TimelineObject(
                    timelineObject = timelineObject,
                    onPostClick = onPostClick
                )
            }
        }
    }
}

// Esta función actúa como tu "repartidor" (Dispatcher)
@Composable
fun TimelineObject(
    timelineObject: TimelineObject,
    onPostClick: (String) -> Unit,
) {
    when (timelineObject) {
        is TimelineObject.PostObject -> PostCardExpanded(post = timelineObject.content)
        is TimelineObject.CarouselObject -> CarouselCardExpanded(carousel = timelineObject.content)
        is TimelineObject.TitleObject -> TitleHeader(title = timelineObject.content)
        is TimelineObject.TrendObject -> TrendSection(trendCategory = timelineObject.content)
    }
}




/**
 * Interfaz sellada que describe el estado de la lista de posts.
 */
sealed interface TimelineUiState {
    /**
     * El timeline se está cargando.
     */
    data object Loading : TimelineUiState

    /**
     * El timeline se ha cargado con éxito.
     */
    data class Success(
        /**
         * La lista de objects que se mostrarán.
         */
        val timelineObjects: List<TimelineObject>,
    ) : TimelineUiState
}
*/