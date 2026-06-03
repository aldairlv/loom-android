package com.loom.feature.eventeditor.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun EventEditorScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    viewModel: CreateEventViewModel = hiltViewModel()
) {
    EventEditorScreen()
}


@Composable
internal fun EventEditorScreen() {

}