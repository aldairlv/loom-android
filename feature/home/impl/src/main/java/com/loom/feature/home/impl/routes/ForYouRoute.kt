package com.loom.feature.home.impl.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.ui.feed.feedObjects


@Composable
fun ForYouRoute(
    modifier: Modifier = Modifier,
    onCommentRepostClick: (com.loom.core.model.data.PostFeedItem) -> Unit = {},
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ForYouRoute(
        uiState = uiState,
        onClickLike = viewModel::onClickLike,
        onComment = viewModel::onComment,
        onQuickRepost = viewModel::onQuickRepost,
        onCommentRepost = onCommentRepostClick,
        onShare = viewModel::onShare,
        onFollowClick = viewModel::onFollowClick,
        onLoadMore = viewModel::loadMore,
        onRefresh = { viewModel.fetchPosts(isLoadMore = false) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ForYouRoute(
    uiState: ForYouUiState,
    onClickLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onQuickRepost: (String) -> Unit,
    onCommentRepost: (com.loom.core.model.data.PostFeedItem) -> Unit,
    onShare: (String) -> Unit,
    onFollowClick: (String, String, Boolean) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is ForYouUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ForYouUiState.Success -> {
                key("home_foryou") {
                    val listState = rememberLazyListState()
                    val shouldLoadMore = remember {
                        derivedStateOf {
                            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                            lastVisibleItemIndex >= listState.layoutInfo.totalItemsCount - 3
                        }
                    }

                    LaunchedEffect(shouldLoadMore.value) {
                        if (shouldLoadMore.value) {
                            onLoadMore()
                        }
                    }

                    PullToRefreshBox(
                        state = rememberPullToRefreshState(),
                        isRefreshing = uiState.isFetchingMore && listState.firstVisibleItemIndex == 0,
                        onRefresh = onRefresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            feedObjects(
                                objects = uiState.objects,
                                onClickLike = onClickLike,
                                onComment = onComment,
                                onQuickRepost = onQuickRepost,
                                onCommentRepost = onCommentRepost,
                                onShare = onShare,
                                onFollowClick = onFollowClick
                            )

                            if (uiState.isFetchingMore && listState.firstVisibleItemIndex != 0) {
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
            }
            is ForYouUiState.Error -> {
                Text(
                    text = "Error loading feed",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
