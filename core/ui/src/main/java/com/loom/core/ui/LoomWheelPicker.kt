package com.loom.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoomWheelPicker(
    items: List<String>,
    initialIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = if (initialIndex in items.indices) initialIndex else 0,
        pageCount = { items.size }
    )

    // Notificar cambio de selección
    LaunchedEffect(pagerState.currentPage) {
        onItemSelected(pagerState.currentPage)
    }

    // Sincronizar si el initialIndex cambia externamente (opcional, pero útil)
    LaunchedEffect(initialIndex) {
        if (initialIndex in items.indices && initialIndex != pagerState.currentPage) {
            pagerState.animateScrollToPage(initialIndex)
        }
    }

    Box(
        modifier = modifier
            .height(110.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Franja de selección central
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = items[page],
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (pagerState.currentPage == page)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontWeight = if (pagerState.currentPage == page) FontWeight.Bold else FontWeight.Normal,
                    fontSize = if (pagerState.currentPage == page) 16.sp else 14.sp
                )
            }
        }
    }
}
