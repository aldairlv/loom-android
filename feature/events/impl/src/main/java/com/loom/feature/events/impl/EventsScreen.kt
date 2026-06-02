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
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EventsScreen(
        uiState = uiState,
        onClickLike = viewModel::onClickLike,
        onQuickRepost = viewModel::onQuickRepost,
        onShare = viewModel::onShare,
        onFollowClick = viewModel::onFollowClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun EventsScreen(
    uiState: EventsUiState,
    onClickLike: (String) -> Unit,
    onQuickRepost: (String) -> Unit,
    onShare: (String) -> Unit,
    onFollowClick: (String, String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val tabs = listOf("Próximos", "Hoy", "Mañana", "Fin de semana")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    var topBarHeight by remember { mutableFloatStateOf(0f) }

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

                    // Tabs Row
                    SecondaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        containerColor = Color.Transparent,
                        divider = {}
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) { _ ->
            EventsPage(
                uiState = uiState,
                onClickLike = onClickLike,
                onQuickRepost = onQuickRepost,
                onShare = onShare,
                onFollowClick = onFollowClick
            )
        }
    }
}

@Composable
private fun EventsPage(
    uiState: EventsUiState,
    onClickLike: (String) -> Unit,
    onQuickRepost: (String) -> Unit,
    onShare: (String) -> Unit,
    onFollowClick: (String, String, Boolean) -> Unit,
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
                        onFollowClick = onFollowClick
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
            onClickLike = {},
            onQuickRepost = {},
            onShare = {},
            onFollowClick = { _, _, _ -> }
        )
    }
}
