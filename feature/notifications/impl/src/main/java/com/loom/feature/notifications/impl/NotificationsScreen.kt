package com.loom.feature.notifications.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.R.drawable
import com.loom.core.ui.tabs.TabsBar
import com.loom.feature.notifications.impl.routes.ActivityRoute
import com.loom.feature.notifications.impl.routes.AskRoute
import com.loom.feature.notifications.impl.routes.ChatsRoute

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    val visibleTabs = remember(tabs) { tabs.filter { it.enabled } }
    val pagerState = rememberPagerState { visibleTabs.size }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column {
                    // Row 1: Logo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = drawable.r_o_o_m_2),
                            contentDescription = "Logo",
                            modifier = Modifier.size(40.dp).padding(2.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                    
                    // Row 2: Tabs + Settings
                    TabsBar(
                        tabs = visibleTabs,
                        pagerState = pagerState,
                        onSettingsClick = { /* Handle settings */ }
                    )
                }
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (visibleTabs[page].key) {
                "activity" -> ActivityRoute()
                "chats" -> ChatsRoute()
                "ask" -> AskRoute()
            }
        }
    }
}
