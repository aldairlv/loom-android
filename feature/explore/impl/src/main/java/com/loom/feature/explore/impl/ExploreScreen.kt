package com.loom.feature.explore.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.TimelineObject
//import com.loom.core.ui.TimelineUiState
//import com.loom.core.ui.timeline


@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel(),

) {
    //val timelineState by viewModel.timelineState.collectAsStateWithLifecycle()
    ExploreScreen(
        //timelineState = timelineState,
        onPostClick = { /* Navegar al detalle */ },

        modifier = modifier,
    )
}

@Composable
internal fun ExploreScreen(
    //timelineState: TimelineUiState,
    onPostClick: (String) -> Unit,

    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("forYou:feed"),
            verticalArrangement = Arrangement.spacedBy(5.dp) // Espacio entre PostCards
        ) {
           /* timeline(
                timelineState = timelineState,
                onPostClick = onPostClick
            )*/

            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }

    }
}
/*
@Preview(showBackground = true, name = "Explore con Datos")
@Composable
fun ExploreScreenPopulatedTimelinePreview(
    @PreviewParameter(ExploreScreenPreviewParameterProvider::class)
    timelineObjects: List<TimelineObject>,
) {
    LoomTheme {
        ExploreScreen(
            timelineState = TimelineUiState.Success(
                timelineObjects = timelineObjects,
            ),
            onPostClick = {}
        )
    }
}
*/