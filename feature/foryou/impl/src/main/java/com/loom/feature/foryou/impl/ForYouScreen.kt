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
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.Post
import com.loom.core.model.data.TimelineObject
import com.loom.core.ui.TimelinePreviewParameterProvider
import com.loom.core.ui.TimelineUiState
import com.loom.core.ui.timeline


@Composable
fun ForYouScreen(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel(),
) {
    val timelineState by viewModel.timelineState.collectAsStateWithLifecycle()
    ForYouScreen(
        timelineState = timelineState,
        onPostClick = { /* Navegar al detalle */ },
        modifier = modifier,
    )
}

@Composable
internal fun ForYouScreen(
    timelineState: TimelineUiState,
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
            verticalArrangement = Arrangement.spacedBy(5.dp) // Espacio entre PostCards
        ) {
            timeline(
                timelineState = timelineState,
                onPostClick = onPostClick
            )

            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }

        // Pantalla de carga (opcional, ya que postsFeed puede manejar Loading)
        /*
        if (feedState is PostFeedUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
        */
    }
}

@Preview(showBackground = true, name = "Timeline con Datos")
@Composable
fun ForYouScreenPopulatedTimelinePreview(
    @PreviewParameter(TimelinePreviewParameterProvider::class)
    timelineObjects: List<TimelineObject>,
) {
    LoomTheme {
        ForYouScreen(
            timelineState = TimelineUiState.Success(
                timelineObjects = timelineObjects,
            ),
            onPostClick = {}
        )
    }
}