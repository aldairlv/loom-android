package com.loom.core.database.di

import android.content.Context
import androidx.room.Room
import com.loom.core.database.LoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesLoomDatabase(
        @ApplicationContext context: Context,
    ): LoomDatabase = Room.databaseBuilder(
        context,
        LoomDatabase::class.java,
        "loom-database",
    ).build()
}