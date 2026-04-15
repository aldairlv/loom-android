package com.loom.core.data.di

import com.loom.core.data.repository.OfflineFirstPostRepository
import com.loom.core.data.repository.PostRepository
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
    internal abstract fun bindsPostRepository(
        postRepository: OfflineFirstPostRepository
    ): PostRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}