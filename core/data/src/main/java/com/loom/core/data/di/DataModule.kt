package com.loom.core.data.di

import com.loom.core.data.repository.AuthRepository
import com.loom.core.data.repository.DataTokenManager
import com.loom.core.data.repository.EventsRepository
import com.loom.core.data.repository.HomeRepository
import com.loom.core.data.repository.NotificationsRepository
import com.loom.core.data.repository.OfflineFirstAuthRepository
import com.loom.core.data.repository.OfflineFirstEventsRepository
import com.loom.core.data.repository.OfflineFirstHomeRepository
import com.loom.core.data.repository.OfflineFirstNotificationsRepository
import com.loom.core.data.repository.OfflineFirstProfileRepository
import com.loom.core.data.repository.OfflineFirstSettingsRepository
import com.loom.core.data.repository.OfflineFirstUserDataRepository
import com.loom.core.data.repository.OfflineFirstUserRepository
import com.loom.core.data.repository.ProfileRepository
import com.loom.core.data.repository.SettingsRepository
import com.loom.core.data.repository.TimelineRepository
import com.loom.core.data.repository.UserRepository
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.data.util.ConnectivityManagerNetworkMonitor
import com.loom.core.data.util.NetworkMonitor
import com.loom.core.network.retrofit.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository

    @Binds
    internal abstract fun bindsTokenManager(
        dataTokenManager: DataTokenManager,
    ): TokenManager

    @Binds
    internal abstract fun bindsAuthRepository(
        authRepository: OfflineFirstAuthRepository
    ): AuthRepository

    @Binds
    internal abstract fun bindsHomeRepository(
        eventsFeedRepository: OfflineFirstHomeRepository
    ): HomeRepository

    @Binds
    internal abstract fun bindsEventsRepository(
        eventsRepository: OfflineFirstEventsRepository
    ): EventsRepository

    @Binds
    internal abstract fun bindsUserRepository(
        userRepository: OfflineFirstUserRepository
    ): UserRepository

    @Binds
    internal abstract fun bindsProfileRepository(
        profileRepository: OfflineFirstProfileRepository
    ): ProfileRepository

    @Binds
    internal abstract fun bindsSettingsRepository(
        settingsRepository: OfflineFirstSettingsRepository
    ): SettingsRepository

    @Binds
    internal abstract fun bindsNotificationsRepository(
        notificationsRepository: OfflineFirstNotificationsRepository
    ): NotificationsRepository

}
