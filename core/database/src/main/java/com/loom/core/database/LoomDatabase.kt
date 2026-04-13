package com.loom.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loom.core.database.dao.PostDao
import com.loom.core.database.model.PostEntity
import com.loom.core.database.util.InstantConverter

@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(InstantConverter::class)
internal abstract class LoomDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}