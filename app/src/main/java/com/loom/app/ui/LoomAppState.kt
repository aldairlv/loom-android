package com.loom.app.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.NavKey
import com.loom.app.navigation.ALL_TOP_LEVEL_DESTINATIONS
import com.loom.app.navigation.TOP_LEVEL_NAV_ITEMS
import com.loom.core.data.repository.ExploreRepository
import com.loom.core.data.repository.TimelineRepository
import com.loom.core.data.util.NetworkMonitor
import com.loom.core.navigation.NavigationState
import com.loom.core.navigation.rememberNavigationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn



@Composable
fun rememberLoomAppState(
    startKey: NavKey,
    networkMonitor: NetworkMonitor,
    timelineRepository: TimelineRepository,
    exploreRepository: ExploreRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),

    ): LoomAppState {

    //val navigationState = rememberNavigationState(startKey, TOP_LEVEL_NAV_ITEMS.keys)
    val navigationState = rememberNavigationState(startKey, ALL_TOP_LEVEL_DESTINATIONS)

    return remember(
        navigationState,
        coroutineScope,
        networkMonitor,
        timelineRepository,
        exploreRepository,

    ) {
        LoomAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
            timelineRepository = timelineRepository,
            exploreRepository = exploreRepository,
            //postRepository = postRepository,
        )
    }
}

@Stable
class LoomAppState(
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    timelineRepository: TimelineRepository,
    exploreRepository: ExploreRepository,

    ) {

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

}

