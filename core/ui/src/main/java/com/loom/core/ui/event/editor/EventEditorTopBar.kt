package com.loom.core.ui.event.editor

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEditorTopBar(
    onClose: () -> Unit,
    onCreateClick: () -> Unit,
    isPublishing: Boolean = false
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onClose, enabled = !isPublishing) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        actions = {
            TextButton(
                onClick = onCreateClick,
                enabled = !isPublishing
            ) {
                Text(
                    text = if (isPublishing) "Creating..." else "Create",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            IconButton(onClick = { /* More actions */ }, enabled = !isPublishing) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    )
}
