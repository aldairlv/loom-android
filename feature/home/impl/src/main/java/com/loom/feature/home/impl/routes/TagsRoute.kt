package com.loom.feature.home.impl.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.loom.core.ui.post.feedPosts


@Composable
fun TagsRoute(
    modifier: Modifier = Modifier,
    viewModel: TagsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TagsRoute(
        uiState = uiState,
        onClickLike = viewModel::onClickLike,
        onComment = viewModel::onComment,
        onRepost = viewModel::onRepost,
        onShare = viewModel::onShare,
        onLoadMore = viewModel::loadMore,
        modifier = modifier
    )
}

@Composable
internal fun TagsRoute(
    uiState: TagsUiState,
    onClickLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onRepost: (String) -> Unit,
    onShare: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is TagsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is TagsUiState.Success -> {
                key("home_tags") {
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

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        feedPosts(
                            posts = uiState.posts,
                            onClickLike = onClickLike,
                            onComment = onComment,
                            onRepost = onRepost,
                            onShare = onShare
                        )

                        if (uiState.isFetchingMore) {
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
            is TagsUiState.Error -> {
                Text(
                    text = "Error loading tags feed",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
