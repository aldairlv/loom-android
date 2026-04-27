package com.loom.core.database.dao
import androidx.room.Dao
import androidx.room.Upsert
import com.loom.core.database.model.TitleEntity


@Dao
interface TitleDao {
    @Upsert
    suspend fun upsertTitle(title: TitleEntity)

    suspend fun upsertTitle(id: String, text: String) {
        upsertTitle(TitleEntity(id, text))
    }
}