package com.loom.feature.profile.impl

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.UserAccountProfile
import com.loom.core.ui.profile.CombinedScrollConnection
import com.loom.core.ui.profile.CustomCollapsingHeader
import com.loom.core.ui.profile.ProfileHeaderContent
import com.loom.core.ui.profile.ProfileTabsRow
import com.loom.core.ui.tabs.TabItem
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    ProfileScreen(
        tabs = tabs,
        profileUiState = profileUiState,
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun ProfileScreen(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>,
    profileUiState: ProfileUiState,
    tabContent: @Composable (ProfileTab) -> Unit = { ProfileTabPage(it) },
) {
    val headerState = rememberTopAppBarState()
    val exitUntilCollapsedBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = headerState,
    )
    val enterAlwaysBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val combinedScrollConnection = remember(exitUntilCollapsedBehavior, enterAlwaysBehavior) {
        CombinedScrollConnection(exitUntilCollapsedBehavior, enterAlwaysBehavior)
    }

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(combinedScrollConnection),
        topBar = {
            Column {
                CustomCollapsingHeader(
                    scrollBehavior = exitUntilCollapsedBehavior,
                    onHeightKnown = { heightPx ->
                        headerState.heightOffsetLimit = -heightPx.toFloat()
                    },
                ) {
                    when (profileUiState) {
                        is ProfileUiState.Loading -> {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        is ProfileUiState.Success -> {
                            ProfileHeaderContent(
                                profile = profileUiState.profile,
                                isEditMode = false,
                                onEditClick = {}
                            )
                        }
                        is ProfileUiState.Error -> {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text(text = profileUiState.message ?: "Error al cargar perfil")
                            }
                        }
                    }
                }

                TopAppBar(
                    title = {
                        ProfileTabsRow(
                            pagerState = pagerState,
                            tabs = tabs,
                            modifier = Modifier.fillMaxWidth(),
                            onTabSelected = { index ->
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    },
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    scrollBehavior = enterAlwaysBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                )
            }
        },
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) { page ->
            val tabItem = tabs[page]
            val profileTab = when (tabItem.key) {
                ProfileTab.Posts.key -> ProfileTab.Posts
                ProfileTab.Likes.key -> ProfileTab.Likes
                ProfileTab.FollowingUsers.key -> ProfileTab.FollowingUsers
                else -> return@HorizontalPager
            }
            tabContent(profileTab)
        }
    }
}





@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenPreview() {
    LoomTheme {
        Surface {
            ProfileScreen(
                tabs = listOf(
                    TabItem(key = ProfileTab.Posts.key, title = ProfileTab.Posts.title),
                    TabItem(key = ProfileTab.Likes.key, title = ProfileTab.Likes.title),
                    TabItem(key = ProfileTab.FollowingUsers.key, title = ProfileTab.FollowingUsers.title),
                ),
                profileUiState = ProfileUiState.Success(
                    UserAccountProfile(
                        id = "1",
                        user = "aldair_id",
                        username = "aldair",
                        displayName = "Aldair",
                        bio = "Bio description",
                        city = "City",
                        timezone = "UTC",
                        canBeFollowed = true,
                        avatarUrl = null,
                        bannerUrl = null,
                        locationCoords = null
                    )
                ),
                tabContent = { tab ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Content for ${tab.title}")
                    }
                }
            )
        }
    }
}
