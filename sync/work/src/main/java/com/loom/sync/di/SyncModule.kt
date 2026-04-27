package com.loom.sync.di

import com.loom.sync.status.StubSyncSubscriber
import com.loom.sync.status.SyncSubscriber
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {
    @Binds
    fun bindSyncSubscriber(
        syncSubscriber: StubSyncSubscriber,
    ): SyncSubscriber
}