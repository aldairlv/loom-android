package com.loom.core.database.di

import com.loom.core.database.LoomDatabase
import com.loom.core.database.dao.PostDao
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
}