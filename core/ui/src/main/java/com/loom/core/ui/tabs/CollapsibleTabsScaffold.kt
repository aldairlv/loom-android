package com.loom.core.ui.tabs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.theme.LoomTheme
import kotlinx.coroutines.launch


@Composable
fun CollapsibleTabsScaffold(
    tabs: List<TabItem>,
    topBar: @Composable () -> Unit,
    tabsBar: @Composable (PagerState) -> Unit,
    modifier: Modifier = Modifier,
    headerHeight: Dp = 96.dp,
    pageContent: @Composable (Int, TabItem) -> Unit,

    ) {
    val pagerState = rememberPagerState { tabs.size }
    val headerState = rememberCollapsibleHeaderState()
    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.getTop(density)
    val headerHeightPx = with(density) { headerHeight.toPx() }
    val totalMaxOffset = headerHeightPx + statusBarHeight
    val nestedScrollConnection = rememberCollapsibleHeaderNestedScrollConnection(
        state = headerState,
        headerHeightPx = totalMaxOffset,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        TabsPager(
            tabs = tabs,
            pagerState = pagerState,
            headerHeightPx = headerHeightPx,
            headerOffsetPx = headerState.offsetPx,
            pageContent = pageContent,
        )
        CollapsibleHeader(
            headerHeight = headerHeight,
            headerOffsetPx = headerState.offsetPx,
        ) {
            topBar()
            tabsBar(pagerState)
        }
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CollapsibleTabsScaffoldPreview() {
    val tabs = listOf(
        TabItem("1", "Tab 1"),
        TabItem("2", "Tab 2"),
        TabItem("3", "Tab 3"),
    )
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CollapsibleTabsScaffold(
                tabs = tabs,
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Collapsible Header",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                tabsBar = { pagerState ->
                    val scope = rememberCoroutineScope()
                    Column {
                        TabRow(selectedTabIndex = pagerState.currentPage) {
                            tabs.forEachIndexed { index, tabItem ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    text = { Text(tabItem.title) }
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                },
                pageContent = { _, tabItem ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(50) { index ->
                            Text(
                                text = "Item $index in ${tabItem.title}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}
