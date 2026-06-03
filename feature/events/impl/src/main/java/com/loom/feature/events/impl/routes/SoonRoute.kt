package com.loom.feature.events.impl.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.ui.feed.feedObjects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoonRoute(
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SoonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is SoonUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is SoonUiState.Success -> {
                val listState = rememberLazyListState()
                val shouldLoadMore = remember {
                    derivedStateOf {
                        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                        lastVisibleItemIndex >= listState.layoutInfo.totalItemsCount - 3
                    }
                }

                LaunchedEffect(shouldLoadMore.value) {
                    if (shouldLoadMore.value) {
                        viewModel.loadMore()
                    }
                }

                val isAtTop by remember {
                    derivedStateOf { listState.firstVisibleItemIndex == 0 }
                }

                PullToRefreshBox(
                    state = rememberPullToRefreshState(),
                    isRefreshing = state.isFetchingMore && isAtTop,
                    onRefresh = { viewModel.fetchSoonEvents(isLoadMore = false) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        feedObjects(
                            objects = state.objects,
                            onClickLike = viewModel::onClickLike,
                            onComment = { },
                            onQuickRepost = viewModel::onQuickRepost,
                            onCommentRepost = { },
                            onShare = viewModel::onShare,
                            onFollowClick = viewModel::onFollowClick,
                            onEventClick = onEventClick
                        )

                        if (state.isFetchingMore && listState.firstVisibleItemIndex != 0) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                }
                            }
                        }
                    }
                }
            }
            is SoonUiState.Error -> {
                Text(
                    text = "Error loading events",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
