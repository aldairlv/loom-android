package com.loom.feature.profile.impl.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.ui.profile.feedFollowingUsers

@Composable
fun FollowingUsersRoute(
    modifier: Modifier = Modifier,
    viewModel: FollowingUsersViewModel = hiltViewModel(),
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
        FollowingUsersRoute(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            onUnfollow = viewModel::onUnfollow,
            onBlock = viewModel::onBlock,
            onLoadMore = viewModel::loadMore,
        )
    }
}

@Composable
internal fun FollowingUsersRoute(
    modifier: Modifier = Modifier,
    uiState: FollowingUsersUiState,
    onUnfollow: (String) -> Unit,
    onBlock: (String) -> Unit,
    onLoadMore: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is FollowingUsersUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is FollowingUsersUiState.Success -> {
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
                                    text = "No sigues a nadie aún",
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        feedFollowingUsers(
                            followingUsers = uiState.followingUsers,
                            onUnfollow = onUnfollow,
                            onBlock = onBlock,
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
            is FollowingUsersUiState.Error -> {
                Text(
                    text = uiState.message ?: "Error loading following users",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
