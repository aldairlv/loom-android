package com.loom.core.data.repository


import com.loom.core.model.data.DarkThemeConfig
import com.loom.core.model.data.ThemeBrand
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.loom.core.model.data.UserData
import com.loom.core.datastore.LoomPreferencesDataSource


internal class OfflineFirstUserDataRepository @Inject constructor(
    private val loomPreferencesDataSource: LoomPreferencesDataSource,

) : UserDataRepository {

    override val userData: Flow<UserData> =
        loomPreferencesDataSource.userData

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        loomPreferencesDataSource.setThemeBrand(themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        loomPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        loomPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        loomPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }
}
