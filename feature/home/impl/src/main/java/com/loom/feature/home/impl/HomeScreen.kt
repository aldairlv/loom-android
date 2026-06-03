package com.loom.feature.home.impl

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Text
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.ui.post.DraggableCreatePostButton
import com.loom.core.ui.tabs.CollapsibleTabsScaffold
import com.loom.core.ui.tabs.TabItem
import com.loom.core.ui.tabs.TabsBar
import com.loom.core.ui.tabs.TabsSettingsSheet
import com.loom.core.navigation.Navigator

@Composable
fun HomeScreen(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    onCommentClick: (String) -> Unit = {},
    onRepostWithComment: (com.loom.core.model.data.PostFeedItem) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    var showCreatePost by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        HomeScreen(
            tabs = tabs,
            onToggleTab = viewModel::toggleTab,
            tabContent = { tab ->
                HomeTabPage(
                    tab = tab,
                    navigator = navigator,
                    onCommentClick = onCommentClick,
                    onRepostWithComment = onRepostWithComment
                )
            }
        )
        DraggableCreatePostButton(
            onClick = onCreateClick
        )
    }
}


@Composable
internal fun HomeScreen(
    tabs: List<TabItem>,
    onToggleTab: (String, Boolean) -> Unit,
    tabContent: @Composable (HomeTab) -> Unit,
){
    var showSettings by rememberSaveable { mutableStateOf(false) }
    val visibleTabs = remember(tabs) { tabs.filter { it.enabled } }

    CollapsibleTabsScaffold(
        tabs = visibleTabs,
        topBar = { HomeTopBar() },
        tabsBar = { pagerState ->
            TabsBar(
                tabs = visibleTabs,
                pagerState = pagerState,
                onSettingsClick = { showSettings = true },
            )
        },
        modifier = Modifier.imePadding(),
    ) { _, tab ->
        // tab aquí es TabItem, necesitas mapear a HomeTab
        val eventTab = when (tab.key) {
            HomeTab.ForYou.key -> HomeTab.ForYou
            HomeTab.Following.key -> HomeTab.Following
            HomeTab.Tags.key -> HomeTab.Tags

            else -> return@CollapsibleTabsScaffold
        }
        tabContent(eventTab)
    }

    if (showSettings) {
        TabsSettingsSheet(
            tabs = tabs,
            onDismiss = { showSettings = false },
            onToggleTab = onToggleTab,
        )
    }
}


@Preview(name = "Light Mode", showBackground = true, showSystemUi = false)
@Preview(name = "Dark Mode", showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen(
                tabs = listOf(
                    TabItem(HomeTab.ForYou.key, HomeTab.ForYou.title),
                    TabItem(HomeTab.Following.key, HomeTab.Following.title),
                    TabItem(HomeTab.Tags.key, HomeTab.Tags.title),
                ),
                onToggleTab = { _, _ -> },
                tabContent = { tab ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Content for ${tab.title}")
                    }
                }
            )
        }
    }
}
