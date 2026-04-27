package com.loom.core.data.di

//import com.loom.core.data.repository.OfflineFirstPostRepository
import com.loom.core.data.repository.OfflineFirstTimelineRepository
import com.loom.core.data.repository.OfflineFirstUserDataRepository
//import com.loom.core.data.repository.PostRepository
import com.loom.core.data.repository.TimelineRepository
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.data.util.ConnectivityManagerNetworkMonitor
import com.loom.core.data.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsTimelineRepository(
        timelineRepository: OfflineFirstTimelineRepository
    ): TimelineRepository

    /*
    @Binds
    internal abstract fun bindsPostRepository(
        postRepository: OfflineFirstPostRepository
    ): PostRepository
    */

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository
}