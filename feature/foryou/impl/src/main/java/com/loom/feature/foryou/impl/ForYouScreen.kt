package com.loom.feature.foryou.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.loom.core.ui.PostFeedUiState
import com.loom.core.ui.postsFeed
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.loom.core.ui.PostFeedPreviewParameterProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.loom.core.designsystem.theme.LoomTheme // Cambia por el nombre de tu tema real
import com.loom.core.model.data.Post


@Composable
fun ForYouScreen(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel(),
) {
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()

    ForYouScreen(
        feedState = feedState,
        onPostClick = { /* Navegar al detalle */ },
        modifier = modifier,
    )
}

@Composable
internal fun ForYouScreen(
    feedState: PostFeedUiState,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("forYou:feed"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre PostCards
        ) {
            // Usamos tu extensión de LazyListScope
            postsFeed(
                feedState = feedState,
                onPostClick = onPostClick
            )

            // Espacio extra al final para que el último post no quede pegado a la barra
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }

        // Pantalla de carga (opcional, ya que postsFeed puede manejar Loading)
        /*if (feedState is PostFeedUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }*/
    }
}

@Preview(showBackground = true, name = "Feed con Datos")
@Composable
fun ForYouScreenPopulatedFeedPreview(
    @PreviewParameter(PostFeedPreviewParameterProvider::class)
    posts: List<Post>,
) {
    LoomTheme {
        ForYouScreen(
            feedState = PostFeedUiState.Success(
                feed = posts,
            ),
            onPostClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Feed Cargando")
@Composable
fun ForYouScreenLoadingPreview() {
    LoomTheme {
        ForYouScreen(
            feedState = PostFeedUiState.Loading,
            onPostClick = {}
        )
    }
}