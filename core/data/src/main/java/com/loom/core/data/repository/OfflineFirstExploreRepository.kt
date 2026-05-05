package com.loom.core.data.repository

import com.loom.core.common.network.Dispatcher
import com.loom.core.common.network.LoomDispatchers.IO
import com.loom.core.data.Synchronizer
import com.loom.core.data.model.asInternalCarouselEntity
import com.loom.core.data.model.asInternalCarouselItemEntities
import com.loom.core.data.model.asInternalTrendTagEntity
import com.loom.core.data.model.asInternalPostEntity
import com.loom.core.data.model.asInternalTimelineEntity
import com.loom.core.data.model.asInternalTrendEntity
import com.loom.core.data.model.asInternalTrendItemEntities
import com.loom.core.data.model.asInternalUserEntity
import com.loom.core.data.model.asInternalUserPostEntity
import com.loom.core.data.suspendRunCatching
import com.loom.core.database.dao.CarouselDao
import com.loom.core.database.dao.EventDao
import com.loom.core.database.dao.PostDao
import com.loom.core.database.dao.TagDao
import com.loom.core.database.dao.TimelineDao
import com.loom.core.database.dao.TimelineMetadataDao
import com.loom.core.database.dao.TitleDao
import com.loom.core.database.dao.TrendDao
import com.loom.core.database.dao.UserDao
import com.loom.core.database.model.PostEntity
import com.loom.core.database.model.TagEntity
import com.loom.core.database.model.TimelineMetadataEntity
import com.loom.core.database.model.TrendEntity
import com.loom.core.database.model.TrendItemEntity
import com.loom.core.database.model.asExternalModel
import com.loom.core.model.enum.TimelineCategory
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkCarouselElementObjectEvent
import com.loom.core.network.model.NetworkObjectTrend
import com.loom.core.network.model.NetworkCarouselElementObjectUser
import com.loom.core.network.model.NetworkObjectCarousel
import com.loom.core.network.model.NetworkObjectPost
import com.loom.core.network.model.NetworkObjectTitle
import com.loom.core.network.model.NetworkObjectTrendElementTag
import com.loom.core.network.model.NetworkObjectTrendElementVideo
import com.loom.core.network.model.NetworkObjectsResponse
import com.loom.core.network.model.NetworkTimelineResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.map

class OfflineFirstExploreRepository @Inject constructor(
    private val timelineDao: TimelineDao,
    private val timelineMetadataDao: TimelineMetadataDao,
    private val postDao: PostDao,
    private val carouselDao: CarouselDao,
    private val titleDao: TitleDao,
    private val userDao: UserDao,
    private val eventDao: EventDao,
    private val network: LoomNetworkDataSource,
    private val trendDao: TrendDao,
    private val tagDao: TagDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : ExploreRepository {

    private var isFirstLoad = true

    override fun getObjects(timelineCategory: TimelineCategory) =
        timelineDao.getTimeline(timelineCategory.value)
            .map { it.map { entity -> entity.asExternalModel() } }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {

        val isAppInUse = !isFirstLoad

        return if (isAppInUse) {
            syncTimeline(TimelineCategory.EXPLORE, forceRefresh = false)
        } else {
            syncTimeline(TimelineCategory.EXPLORE, forceRefresh = true)
        }
    }

    override suspend fun syncTimeline(
        timelineCategory: TimelineCategory,
        forceRefresh: Boolean
    ): Boolean = suspendRunCatching {
        withContext(ioDispatcher) {
            val shouldStartFromScratch = forceRefresh or isFirstLoad
            val currentCursor = if (shouldStartFromScratch) {
                null
            } else {
                timelineMetadataDao.getMetadata(timelineCategory.value)?.cursor
            }
            val response = network.getExplore(currentCursor)

            if (shouldStartFromScratch) {
                timelineDao.clearTimelineObjects(timelineCategory.value)
                isFirstLoad = false
            }
            processAndSaveResponse(timelineCategory, response)
        }
    }.isSuccess

    private suspend fun processAndSaveResponse(
        timelineCategory: TimelineCategory,
        response: NetworkObjectsResponse
    ) {
        val postsToSave = mutableListOf<PostEntity>()
        val tagsToSave = mutableListOf<TagEntity>()
        val trendsToSave = mutableListOf<TrendEntity>()
        val trendItemsToSave = mutableListOf<TrendItemEntity>()
        response.elements.forEach { netObj ->
            when (netObj) {
                is NetworkObjectTitle -> {
                    titleDao.upsertTitle(netObj.id, netObj.text)
                }
                is NetworkObjectTrend -> {
                    trendsToSave.add(netObj.asInternalTrendEntity())
                    trendItemsToSave.addAll(netObj.asInternalTrendItemEntities())

                    netObj.elements.forEach { element ->
                        if (element is NetworkObjectTrendElementTag) {
                            postsToSave.addAll(element.resource.map { it.asInternalUserPostEntity() })
                            tagsToSave.add(element.asInternalTrendTagEntity())
                        }
                        if (element is NetworkObjectTrendElementVideo) {
                            postsToSave.addAll(element.resource.map { it.asInternalUserPostEntity() })
                        }
                    }
                }
                else -> { throw IllegalArgumentException("Wrong element type: $netObj") }
            }
        }

        if (postsToSave.isNotEmpty()) postDao.upsertPosts(postsToSave)
        if (tagsToSave.isNotEmpty()) tagDao.upsertTags(tagsToSave)

        if (trendsToSave.isNotEmpty()) trendDao.upsertTrends(trendsToSave)
        if (trendItemsToSave.isNotEmpty()) trendDao.upsertTrendItems(trendItemsToSave)


        val entities = response.elements.map { it.asInternalTimelineEntity(timelineCategory.value) }
        timelineDao.upsertTimelineObjects(entities)

        timelineMetadataDao.upsertMetadata(
            TimelineMetadataEntity(
                timelineType = timelineCategory.value,
                cursor = response.cursor,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }


}