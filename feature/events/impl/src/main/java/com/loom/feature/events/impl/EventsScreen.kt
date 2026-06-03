package com.loom.feature.events.impl

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.ui.feed.feedObjects
import com.loom.core.ui.tabs.TabItem
import com.loom.core.ui.tabs.TabsBar
import com.loom.core.ui.tabs.TabsSettingsSheet
import com.loom.feature.events.impl.routes.SoonRoute
import com.loom.feature.eventdetail.api.navigation.EventNavKey
import com.loom.core.navigation.Navigator
import com.loom.core.ui.post.DraggableCreatePostButton
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun EventsScreen(
    navigator: Navigator,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize()) {
        EventsScreen(
            uiState = uiState,
            tabs = tabs,
            onToggleTab = viewModel::toggleTab,
            onClickLike = viewModel::onClickLike,
            onQuickRepost = viewModel::onQuickRepost,
            onShare = viewModel::onShare,
            onFollowClick = viewModel::onFollowClick,
            onEventClick = { id -> navigator.navigate(EventNavKey(id)) },
            modifier = modifier
        )
        DraggableCreatePostButton(
            onClick = onCreateClick
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun EventsScreen(
    uiState: EventsUiState,
    tabs: List<TabItem>,
    onToggleTab: (String, Boolean) -> Unit,
    onClickLike: (String) -> Unit,
    onQuickRepost: (String) -> Unit,
    onShare: (String) -> Unit,
    onFollowClick: (String, String, Boolean) -> Unit,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val visibleTabs = remember(tabs) { tabs.filter { it.enabled } }
    val pagerState = rememberPagerState(pageCount = { visibleTabs.size })
    val coroutineScope = rememberCoroutineScope()
    var topBarHeight by remember { mutableFloatStateOf(0f) }
    var showSettings by remember { mutableStateOf(false) }

    LaunchedEffect(topBarHeight) {
        if (topBarHeight > 0f) {
            scrollBehavior.state.heightOffsetLimit = -topBarHeight
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                modifier = Modifier
                    .onGloballyPositioned { topBarHeight = it.size.height.toFloat() }
                    .offset { IntOffset(0, scrollBehavior.state.heightOffset.roundToInt()) }
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                Column {
                    // Logo Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = com.loom.core.designsystem.R.drawable.r_o_o_m_2),
                            contentDescription = "Logo",
                            modifier = Modifier.size(40.dp).padding(2.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }

                    // Tabs Bar
                    TabsBar(
                        tabs = visibleTabs,
                        pagerState = pagerState,
                        onSettingsClick = { showSettings = true }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) { page ->
            val tab = visibleTabs[page]
            when (tab.key) {
                "upcoming" -> SoonRoute(onEventClick = onEventClick)
                else -> EventsPage(
                    uiState = uiState,
                    onClickLike = onClickLike,
                    onQuickRepost = onQuickRepost,
                    onShare = onShare,
                    onFollowClick = onFollowClick,
                    onEventClick = onEventClick
                )
            }
        }
    }

    if (showSettings) {
        TabsSettingsSheet(
            tabs = tabs,
            onDismiss = { showSettings = false },
            onToggleTab = onToggleTab
        )
    }
}

@Composable
private fun EventsPage(
    uiState: EventsUiState,
    onClickLike: (String) -> Unit,
    onQuickRepost: (String) -> Unit,
    onShare: (String) -> Unit,
    onFollowClick: (String, String, Boolean) -> Unit,
    onEventClick: (String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is EventsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is EventsUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    feedObjects(
                        objects = uiState.events,
                        onClickLike = onClickLike,
                        onComment = { /* Handle comment */ },
                        onQuickRepost = onQuickRepost,
                        onCommentRepost = { /* Handle comment repost */ },
                        onShare = onShare,
                        onFollowClick = onFollowClick,
                        onEventClick = onEventClick
                    )
                }
            }
            is EventsUiState.Error -> {
                Text(
                    text = uiState.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    LoomTheme {
        EventsScreen(
            uiState = EventsUiState.Success(emptyList()),
            tabs = listOf(
                TabItem("upcoming", "Próximos"),
                TabItem("today", "Hoy"),
            ),
            onToggleTab = { _, _ -> },
            onClickLike = {},
            onQuickRepost = {},
            onShare = {},
            onFollowClick = { _, _, _ -> },
            onEventClick = {}
        )
    }
}
