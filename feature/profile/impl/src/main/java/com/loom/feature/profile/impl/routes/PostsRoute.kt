package com.loom.feature.profile.impl.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.ui.post.feedPosts

@Composable
fun PostsRoute(
    modifier: Modifier = Modifier,
    viewModel: PostsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.snackbarMessages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        PostsRoute(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            onClickLike = viewModel::onClickLike,
            onComment = viewModel::onComment,
            onRepost = viewModel::onRepost,
            onShare = viewModel::onShare,
            onLoadMore = viewModel::loadMore,
        )
    }
}

@Composable
internal fun PostsRoute(
    modifier: Modifier = Modifier,
    uiState: PostsUiState,
    onClickLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onRepost: (String) -> Unit,
    onShare: (String) -> Unit,
    onLoadMore: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is PostsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is PostsUiState.Success -> {
                val listState = rememberLazyListState()

                if (!uiState.isEmpty) {
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
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.isEmpty) {
                        item(key = "empty_state") {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxHeight()
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Aun no has publicado nada",
                                    color = Color.White
                                )
                            }
                        }
                    } else {
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
            is PostsUiState.Error -> {
                Text(
                    text = "Error loading posts",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
