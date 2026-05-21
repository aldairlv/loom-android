package com.loom.core.ui.tabs

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.designsystem.theme.LoomTheme
import kotlinx.coroutines.launch


@Composable
fun TabsBar(
    tabs: List<TabItem>,
    pagerState: PagerState,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(modifier = modifier.weight(1f)) {
            PrimaryScrollableTabRow(
                selectedTabIndex = minOf(pagerState.currentPage, tabs.lastIndex),
                edgePadding = 5.dp,
                divider = {},
                containerColor = Color.Transparent,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex = pagerState.currentPage,
                            matchContentSize = true,
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = {
                            Text(
                                text = tab.title,
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            TabsFadeOverlay()
        }

        IconButton(
            onClick = onSettingsClick,
        ) {
            Icon(
                imageVector = LoomIcons.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TabsBarPreview() {
    val tabs = listOf(
        TabItem("foryou", "For You"),
        TabItem("following", "Following"),
        TabItem("tags", "Tags"),
        TabItem("news", "News"),
        TabItem("sports", "Sports"),
    )
    val pagerState = rememberPagerState { tabs.size }

    LoomTheme {
        // Añade un Surface para que tome el color del tema
        Surface(color = MaterialTheme.colorScheme.background) {
            TabsBar(
                tabs = tabs,
                pagerState = pagerState,
                onSettingsClick = {}
            )
        }
    }
}
