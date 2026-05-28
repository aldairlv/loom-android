package com.loom.core.ui.profile

import com.loom.core.ui.tabs.TabItem
import com.loom.core.ui.tabs.TabsFadeOverlay


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
/*

@Composable
fun ProfileTabsBar(
    tabs: List<TabItem>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Red)
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
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TabsBarPreview() {
    val tabs = listOf(
        TabItem("posts", "Posts"),
        TabItem("likes", "Likes"),
        TabItem("following", "Following"),

    )
    val pagerState = rememberPagerState { tabs.size }

    LoomTheme {
        // Añade un Surface para que tome el color del tema
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileTabsBar(
                tabs = tabs,
                pagerState = pagerState
            )
        }
    }
}
*/


@Composable
fun ProfileTabsRow(
    pagerState: PagerState,
    tabs: List<TabItem>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier.fillMaxWidth().padding(end = 18.dp),
        divider = {},
        indicator = { tabPositions ->
            if (pagerState.currentPage < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .width(40.dp), // <- Forzamos un ancho fijo de 40.dp
                    color = Color.White,
                    height = 2.dp
                )
            }
        }
    ) {
        // Tus tabs se quedan exactamente igual
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = { onTabSelected(index) },
                selectedContentColor = Color.White,       // Color cuando está seleccionado
                unselectedContentColor = Color.LightGray,
                text = { Text(tab.title) },
            )
        }
    }
}

