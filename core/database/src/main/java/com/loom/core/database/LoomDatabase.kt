package com.loom.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.loom.core.database.dao.UserAccountProfileDao
import com.loom.core.database.model.UserAccountProfileEntity
import com.loom.core.database.util.InstantConverter

/*
import com.loom.core.database.dao.CarouselDao
import com.loom.core.database.dao.EventDao
import com.loom.core.database.dao.PostDao
import com.loom.core.database.dao.TagDao
import com.loom.core.database.dao.TimelineDao
import com.loom.core.database.dao.TimelineMetadataDao
import com.loom.core.database.dao.TitleDao
import com.loom.core.database.dao.TrendDao
import com.loom.core.database.dao.UserDao
import com.loom.core.database.model.TagEntity
import com.loom.core.database.model.TrendEntity
import com.loom.core.database.model.TrendItemEntity
import com.loom.core.database.model.PostEntity
import com.loom.core.database.model.CarouselEntity
import com.loom.core.database.model.EventEntity
import com.loom.core.database.model.TimelineEntity
import com.loom.core.database.model.TimelineMetadataEntity
import com.loom.core.database.model.TitleEntity
import com.loom.core.database.model.UserEntity
import com.loom.core.database.model.CarouselItemEntity
import com.loom.core.database.model.EventParticipantEntity
*/

@Database(
    entities = [
        /*
        TrendEntity::class,
        TrendItemEntity::class,
        TagEntity::class,
        PostEntity::class,
        TimelineEntity::class,
        TimelineMetadataEntity::class,
        CarouselEntity::class,
        CarouselItemEntity::class,
        TitleEntity::class,
        UserEntity::class,
        EventEntity::class,
        EventParticipantEntity::class,
        */
        UserAccountProfileEntity::class,
               ],
    version = 1,
    exportSchema = true
)
@TypeConverters(InstantConverter::class)
internal abstract class LoomDatabase : RoomDatabase() {
    /*
    abstract fun trendDao(): TrendDao
    abstract fun tagDao(): TagDao
    abstract fun postDao(): PostDao
    abstract fun timelineDao(): TimelineDao
    abstract fun timelineMetadataDao(): TimelineMetadataDao
    abstract fun carouselDao(): CarouselDao
    abstract fun titleDao(): TitleDao
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    */
    abstract fun userAccountProfileDao(): UserAccountProfileDao
}
