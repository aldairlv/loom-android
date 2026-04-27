package com.loom.app.navigation


import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.loom.feature.foryou.api.navigation.ForYouNavKey

import com.loom.core.designsystem.icon.LoomIcons
import com.loom.feature.foryou.api.R as forYouR
/**
 * Type for the top level navigation items in the Loom application.
 */
data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)


val FOR_YOU = TopLevelNavItem(
        selectedIcon = LoomIcons.Home,
        unselectedIcon = LoomIcons.HomeBorder,
        iconTextId = forYouR.string.feature_foryou_api_title,
        titleTextId = forYouR.string.feature_foryou_api_title,
    )

val EVENTS = TopLevelNavItem(
    selectedIcon = LoomIcons.Events,
    unselectedIcon = LoomIcons.EventsBorder,
    iconTextId = forYouR.string.feature_foryou_api_title,
    titleTextId = forYouR.string.feature_foryou_api_title,
)

val SEARCH = TopLevelNavItem(
    selectedIcon = LoomIcons.Search,
    unselectedIcon = LoomIcons.SearchBorder,
    iconTextId = forYouR.string.feature_foryou_api_title,
    titleTextId = forYouR.string.feature_foryou_api_title,
)

val PROFILE = TopLevelNavItem(
    selectedIcon = LoomIcons.Person,
    unselectedIcon = LoomIcons.PersonBorder,
    iconTextId = forYouR.string.feature_foryou_api_title,
    titleTextId = forYouR.string.feature_foryou_api_title,
)

val ACTIVITY = TopLevelNavItem(
    selectedIcon = LoomIcons.Activity,
    unselectedIcon = LoomIcons.ActivityBorder,
    iconTextId = forYouR.string.feature_foryou_api_title,
    titleTextId = forYouR.string.feature_foryou_api_title,
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    ForYouNavKey to FOR_YOU,
)
