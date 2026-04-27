package com.loom.core.database.di

import com.loom.core.database.LoomDatabase
import com.loom.core.database.dao.CarouselDao
import com.loom.core.database.dao.EventDao
import com.loom.core.database.dao.PostDao
import com.loom.core.database.dao.TimelineDao
import com.loom.core.database.dao.TimelineMetadataDao
import com.loom.core.database.dao.TitleDao
import com.loom.core.database.dao.UserDao
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
    fun providesTimelineMetadataDao(
        database: LoomDatabase,
    ): TimelineMetadataDao = database.timelineMetadataDao()

    @Provides
    fun providesCarouselDao(
        database: LoomDatabase,
    ): CarouselDao = database.carouselDao()

    @Provides
    fun providesTitleDao(
        database: LoomDatabase,
    ): TitleDao = database.titleDao()

    @Provides
    fun providesUserDao(
        database: LoomDatabase,
    ): UserDao = database.userDao()

    @Provides
    fun providesEventDao(
        database: LoomDatabase,
    ): EventDao = database.eventDao()

}