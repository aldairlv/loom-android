package com.loom.app.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.loom.app.navigation.TOP_LEVEL_NAV_ITEMS
import com.loom.core.data.repository.PostRepository
import com.loom.core.data.util.NetworkMonitor
import com.loom.core.navigation.NavigationState
import com.loom.core.navigation.rememberNavigationState
import com.loom.feature.foryou.api.navigation.ForYouNavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberLoomAppState(
    networkMonitor: NetworkMonitor,
    postRepository: PostRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),

    ): LoomAppState {
    val navigationState = rememberNavigationState(ForYouNavKey, TOP_LEVEL_NAV_ITEMS.keys)

    return remember(
        navigationState,
        coroutineScope,
        networkMonitor,
        postRepository,
    ) {
        LoomAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
            postRepository = postRepository,
        )
    }
}

@Stable
class LoomAppState(
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    postRepository: PostRepository,

    ) {

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

}

