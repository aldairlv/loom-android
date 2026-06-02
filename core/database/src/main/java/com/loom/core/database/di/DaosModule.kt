package com.loom.core.database.di

import com.loom.core.database.LoomDatabase
import com.loom.core.database.dao.UserAccountProfileDao
import com.loom.core.database.dao.PostDao
import com.loom.core.database.dao.TimelineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesPostDao(
        database: LoomDatabase,
    ): PostDao = database.postDao()

    @Provides
    fun providesTimelineDao(
        database: LoomDatabase,
    ): TimelineDao = database.timelineDao()

    @Provides
    fun providesUserAccountProfileDao(
        database: LoomDatabase,
    ): UserAccountProfileDao = database.userAccountProfileDao()

}
