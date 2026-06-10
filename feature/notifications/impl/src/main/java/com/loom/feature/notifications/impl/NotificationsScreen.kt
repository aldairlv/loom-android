package com.loom.feature.notifications.impl

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.R.drawable
import com.loom.core.ui.tabs.TabsBar
import com.loom.feature.notifications.impl.routes.ActivityRoute
import com.loom.feature.notifications.impl.routes.AskRoute
import com.loom.feature.chats.impl.ChatsRoute

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    val visibleTabs = remember(tabs) { tabs.filter { it.enabled } }
    val pagerState = rememberPagerState { visibleTabs.size }
    
    val shouldShowPermissionSnackbar by viewModel.shouldShowPermissionSnackbar.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionRequested()
    }

    LaunchedEffect(shouldShowPermissionSnackbar) {
        if (shouldShowPermissionSnackbar) {
            val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }

            if (!hasPermission) {
                val result = snackbarHostState.showSnackbar(
                    message = "¿Quieres activar las notificaciones?",
                    actionLabel = "Sí, activar",
                    duration = SnackbarDuration.Long
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                    SnackbarResult.Dismissed -> {
                        viewModel.onDismissPermissionRequest()
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                "chats" -> ChatsRoute(onConversationClick = { /* Navigate to Chat Details */ })
                "ask" -> AskRoute()
            }
        }
    }
}
