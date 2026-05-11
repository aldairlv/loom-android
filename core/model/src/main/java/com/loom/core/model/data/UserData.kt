package com.loom.core.model.data

/**
 * Class summarizing user interest data
 */
data class UserData(
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)

val UserData.isLoggedIn: Boolean
    get() = accessToken.isNotEmpty()
