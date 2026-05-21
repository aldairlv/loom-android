package com.loom.core.ui.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource


@Stable
class CollapsibleHeaderState(initialOffset: Float = 0f) {
    var offsetPx by mutableFloatStateOf(initialOffset)

    internal fun updateOffset(delta: Float, maxOffset: Float) {
        offsetPx = (offsetPx + delta).coerceIn(-maxOffset, 0f)
    }
}

@Composable
fun rememberCollapsibleHeaderState(): CollapsibleHeaderState =
    remember { CollapsibleHeaderState() }

@Composable
fun rememberCollapsibleHeaderNestedScrollConnection(
    state: CollapsibleHeaderState,
    headerHeightPx: Float,
): NestedScrollConnection = remember(state, headerHeightPx) {
    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            state.updateOffset(delta = available.y, maxOffset = headerHeightPx)
            return Offset.Zero
        }
    }
}