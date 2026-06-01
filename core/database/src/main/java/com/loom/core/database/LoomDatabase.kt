package com.loom.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loom.core.database.dao.UserAccountProfileDao
import com.loom.core.database.dao.PostDao
import com.loom.core.database.dao.TimelineDao
import com.loom.core.database.model.UserAccountProfileEntity
import com.loom.core.database.model.PostEntity
import com.loom.core.database.model.TimelineEntity
import com.loom.core.database.util.InstantConverter
import com.loom.core.database.util.PostConverters

@Database(
    entities = [
        UserAccountProfileEntity::class,
        PostEntity::class,
        TimelineEntity::class,
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(InstantConverter::class, PostConverters::class)
abstract class LoomDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun timelineDao(): TimelineDao
    abstract fun userAccountProfileDao(): UserAccountProfileDao
}
