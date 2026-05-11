package com.loom.core.data.repository

import com.loom.core.model.data.DarkThemeConfig
import com.loom.core.model.data.ThemeBrand
import com.loom.core.model.data.UserData
import kotlinx.coroutines.flow.Flow


interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the desired theme brand.
     */
    suspend fun setThemeBrand(themeBrand: ThemeBrand)

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

    /**
     * Sets the auth tokens and user id.
     */
    suspend fun setTokens(accessToken: String, refreshToken: String, userId: String)

    /**
     * Clears the auth tokens and user id.
     */
    suspend fun clearTokens()
}
