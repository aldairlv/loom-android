package com.loom.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.navigation3.runtime.entryProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.loom.app.navigation.TOP_LEVEL_NAV_ITEMS
import com.loom.core.designsystem.component.LoomBackground
import com.loom.core.designsystem.component.LoomNavigationSuiteScaffold
import com.loom.core.designsystem.component.LoomTopAppBar
import com.loom.core.navigation.Navigator
import com.loom.core.navigation.toEntries
import com.loom.feature.foryou.impl.navigation.forYouEntry
import com.loom.feature.explore.impl.navigation.exploreEntry

@Composable
fun LoomApp(
    appState: LoomAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {

    LoomBackground(modifier = modifier) {
        val snackbarHostState = remember { SnackbarHostState() }
        val isOffline by appState.isOffline.collectAsStateWithLifecycle()

        // Manejo de conexión offline
        val notConnectedMessage = "No hay conexión a internet" // Usar stringResource en prod
        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = notConnectedMessage,
                    duration = Indefinite,
                )
            }
        }

        LoomApp(
            appState = appState,
            snackbarHostState = snackbarHostState,
            windowAdaptiveInfo = windowAdaptiveInfo
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,)
internal fun LoomApp(
    appState: LoomAppState,
    snackbarHostState: SnackbarHostState,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val navigator = remember { Navigator(appState.navigationState) }
    // El Scaffold de navegación que adapta entre Barra inferior y Rail lateral
    LoomNavigationSuiteScaffold(
        navigationSuiteItems = {
            TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
                val selected = navKey == appState.navigationState.currentTopLevelKey
                item(
                    selected = selected,
                    onClick = { navigator.navigate(navKey) },
                    icon = {
                        Icon(
                            imageVector = navItem.unselectedIcon,
                            contentDescription = null
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = navItem.selectedIcon,
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(navItem.iconTextId)) },
                )
            }
        },
        windowAdaptiveInfo = windowAdaptiveInfo,
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {

                // Only show the top app bar on top level destinations.
                var shouldShowTopAppBar = false

                if (appState.navigationState.currentKey in appState.navigationState.topLevelKeys) {
                    shouldShowTopAppBar = true
                    val currentDest =
                        TOP_LEVEL_NAV_ITEMS[appState.navigationState.currentTopLevelKey]
                            ?: error("Top level nav item not found for ${appState.navigationState.currentTopLevelKey}")

                    LoomTopAppBar(
                        titleRes = currentDest.titleTextId
                    )

                }
                Box(
                    modifier = Modifier.consumeWindowInsets(
                        if (shouldShowTopAppBar) {
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                        } else {
                            WindowInsets(0, 0, 0, 0)
                        },
                    ),
                ) {

                    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

                    val entryProvider = entryProvider {
                        forYouEntry(navigator)
                        exploreEntry(navigator)
                    }
                    NavDisplay(
                        entries = appState.navigationState.toEntries(entryProvider),
                        sceneStrategy = listDetailStrategy,
                        onBack = { navigator.goBack() },
                    )
                }
            }
        }
    }
}