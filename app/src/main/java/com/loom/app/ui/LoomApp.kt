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
import com.loom.core.model.data.SessionState
import com.loom.core.navigation.Navigator
import com.loom.core.navigation.toEntries
import com.loom.feature.auth.api.navigation.LandingNavKey
import com.loom.feature.auth.impl.navigation.completeRegisterBirthDateEntry
import com.loom.feature.auth.impl.navigation.completeRegisterUsernameEntry
import com.loom.feature.explore.impl.navigation.exploreEntry
import com.loom.feature.auth.impl.navigation.landingEntry
import com.loom.feature.auth.impl.navigation.emailInputEntry
import com.loom.feature.auth.impl.navigation.passwordInputEntry
import com.loom.feature.comments.api.navigation.CommentsNavKey
import com.loom.feature.comments.impl.navigation.commentsEntry
import com.loom.feature.eventdetail.api.navigation.EventNavKey
import com.loom.feature.eventdetail.impl.navigation.eventEntry
import com.loom.feature.eventeditor.api.navigation.CreateEventNavKey
import com.loom.feature.eventeditor.impl.navigation.createEventEntry
import com.loom.feature.events.impl.navigation.eventsEntry
import com.loom.feature.posteditor.api.navigation.CreatePostNavKey
import com.loom.feature.posteditor.impl.navigation.createPostEntry
import com.loom.feature.home.impl.navigation.homeEntry
import com.loom.feature.notifications.impl.navigation.notificationsEntry
import com.loom.feature.profile.impl.navigation.profileEntry
import com.loom.feature.settings.api.navigation.AccountSettingsNavKey
import com.loom.feature.settings.api.navigation.LocationNavKey
import com.loom.feature.settings.api.navigation.SettingsNavKey
import com.loom.feature.settings.impl.navigation.accountSettingsEntry
import com.loom.feature.settings.impl.navigation.locationEntry
import com.loom.feature.settings.impl.navigation.settingsEntry


@Composable
fun LoomApp(
    appState: LoomAppState,
    sessionState: SessionState,
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
            sessionState = sessionState,
            snackbarHostState = snackbarHostState,
            windowAdaptiveInfo = windowAdaptiveInfo
        )
    }
}

@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
)
internal fun LoomApp(
    appState: LoomAppState,
    sessionState: SessionState,
    snackbarHostState: SnackbarHostState,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    //val navigator = remember { Navigator(appState.navigationState) }

    val navigator = remember(appState.navigationState, sessionState) {
        Navigator(
            state = appState.navigationState,
            isLoggedIn = { sessionState is SessionState.LoggedIn },
            landingKey = LandingNavKey
        )
    }

    val entryProvider = entryProvider {
        // Auth
        landingEntry(navigator)
        emailInputEntry(navigator)
        passwordInputEntry(navigator)
        completeRegisterBirthDateEntry(navigator)
        completeRegisterUsernameEntry(navigator)

        // Main
        homeEntry(navigator)
        exploreEntry(navigator)
        createPostEntry(navigator)
        profileEntry(navigator)
        commentsEntry(navigator)
        eventsEntry(navigator)
        eventEntry(navigator)
        createEventEntry(navigator)
        notificationsEntry(navigator)

        // Settings
        settingsEntry(navigator)
        accountSettingsEntry(navigator)
        locationEntry(navigator)
    }

    val isLoggedIn = sessionState is SessionState.LoggedIn
    val currentKey = appState.navigationState.currentKey

    if (isLoggedIn) {
        if (currentKey is CreatePostNavKey ||
            currentKey is SettingsNavKey ||
            currentKey is AccountSettingsNavKey ||
            currentKey is CommentsNavKey ||
            currentKey is LocationNavKey ||
            currentKey is EventNavKey ||
            currentKey is CreateEventNavKey
        ){
            NavDisplay(
                entries = appState.navigationState.toEntries(entryProvider),
                onBack = { navigator.goBack() },
            )
        } else {
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

                        /*
                        if (appState.navigationState.currentKey in appState.navigationState.topLevelKeys) {
                            shouldShowTopAppBar = true
                            val currentDest =
                                TOP_LEVEL_NAV_ITEMS[appState.navigationState.currentTopLevelKey]
                                    ?: error("Top level nav item not found for ${appState.navigationState.currentTopLevelKey}")
    
                            LoomTopAppBar(
                                titleRes = currentDest.titleTextId
                            )
    
                        }
                        */

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
        
    } else {
        // sin bottom bar
       NavDisplay(
           entries = appState.navigationState.toEntries(entryProvider),
           onBack = { navigator.goBack() },
       )
    }


}