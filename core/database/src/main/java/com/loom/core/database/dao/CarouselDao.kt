package com.loom.core.database.dao
/*
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.loom.core.database.model.CarouselEntity
import com.loom.core.database.model.CarouselItemEntity
import com.loom.core.database.model.PopulatedCarousel
import kotlinx.coroutines.flow.Flow

@Dao
interface CarouselDao {
    @Transaction
    @Query("SELECT * FROM carousels WHERE id = :id")
    fun getCarouselById(id: Long): Flow<PopulatedCarousel>

    @Upsert
    suspend fun upsertCarouselEntity(carousel: CarouselEntity)

    @Upsert
    suspend fun upsertCarouselItems(items: List<CarouselItemEntity>)

    @Transaction
    suspend fun upsertCarousel(
        carousel: CarouselEntity,
        items: List<CarouselItemEntity>
    ) {
        upsertCarouselEntity(carousel)
        upsertCarouselItems(items)
    }
}
*/