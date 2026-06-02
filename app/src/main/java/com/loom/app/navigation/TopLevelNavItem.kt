package com.loom.app.navigation


import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.loom.feature.explore.api.navigation.ExploreNavKey

import com.loom.core.designsystem.icon.LoomIcons
import com.loom.feature.auth.api.navigation.LandingNavKey
import com.loom.feature.auth.api.navigation.EmailInputNavKey
import com.loom.feature.auth.api.navigation.PasswordInputNavKey
import com.loom.feature.events.api.navigation.EventsNavKey
import com.loom.feature.home.api.navigation.HomeNavKey
import com.loom.feature.profile.api.navigation.ProfileNavKey
import com.loom.feature.home.api.R as homeR
import com.loom.feature.explore.api.R as exploreR
import com.loom.feature.events.api.R as eventsR
import com.loom.feature.profile.api.R as profileR

/**
 * Type for the top level navigation items in the Loom application.
 */
data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)


val HOME = TopLevelNavItem(
        selectedIcon = LoomIcons.Home,
        unselectedIcon = LoomIcons.HomeBorder,
        iconTextId = homeR.string.feature_home_api_title,
        titleTextId = homeR.string.feature_home_api_title,
    )

val EVENTS = TopLevelNavItem(
    selectedIcon = LoomIcons.Events,
    unselectedIcon = LoomIcons.EventsBorder,
    iconTextId = eventsR.string.feature_events_api_title,
    titleTextId = eventsR.string.feature_events_api_title,
)

val EXPLORE = TopLevelNavItem(
    selectedIcon = LoomIcons.Search,
    unselectedIcon = LoomIcons.SearchBorder,
    iconTextId = exploreR.string.feature_explore_api_title,
    titleTextId = exploreR.string.feature_explore_api_title,
)

val ACTIVITY = TopLevelNavItem(
    selectedIcon = LoomIcons.Activity,
    unselectedIcon = LoomIcons.ActivityBorder,
    iconTextId = homeR.string.feature_home_api_title,
    titleTextId = homeR.string.feature_home_api_title,
)
val PROFILE = TopLevelNavItem(
    selectedIcon = LoomIcons.Person,
    unselectedIcon = LoomIcons.PersonBorder,
    iconTextId = profileR.string.feature_profile_api_title,
    titleTextId = profileR.string.feature_profile_api_title,
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HomeNavKey to HOME,
    ExploreNavKey to EXPLORE,
    EventsNavKey to EVENTS,
    ProfileNavKey to PROFILE,

)

val ALL_TOP_LEVEL_DESTINATIONS = TOP_LEVEL_NAV_ITEMS.keys + LandingNavKey
