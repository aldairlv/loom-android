package com.loom.core.ui.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt


@Composable
fun TabsPager(
    tabs: List<TabItem>,
    pagerState: PagerState,
    headerHeightPx: Float,
    headerOffsetPx: Float,
    pageContent: @Composable (Int, TabItem) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .offset {
                IntOffset(
                    x = 0,
                    y = (headerHeightPx + headerOffsetPx).roundToInt()
                )
            },
        key = { tabs[it].key },
    ) { page ->
        pageContent(page, tabs[page])
    }
}