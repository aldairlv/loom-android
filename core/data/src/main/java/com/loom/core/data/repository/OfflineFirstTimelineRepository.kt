package com.loom.core.data.repository
/*
import com.loom.core.common.network.Dispatcher
import com.loom.core.common.network.LoomDispatchers.IO
import com.loom.core.data.Synchronizer
import com.loom.core.data.model.asInternalPostEntity
import com.loom.core.data.model.asInternalCarouselEntity
import com.loom.core.data.model.asInternalCarouselItemEntities
import com.loom.core.data.model.asInternalEventEntity
import com.loom.core.data.model.asInternalTimelineEntity
import com.loom.core.data.model.asInternalUserEntity
import com.loom.core.data.model.asInternalUserPostEntity
import com.loom.core.data.suspendRunCatching
import com.loom.core.database.dao.CarouselDao
import com.loom.core.database.dao.EventDao
import com.loom.core.database.dao.PostDao
import com.loom.core.database.dao.TimelineDao
import com.loom.core.database.dao.TimelineMetadataDao
import com.loom.core.database.dao.TitleDao
import com.loom.core.database.dao.UserDao
import com.loom.core.database.model.CarouselEntity
import com.loom.core.database.model.CarouselItemEntity
import com.loom.core.database.model.TimelineMetadataEntity
import com.loom.core.database.model.asExternalModel
import com.loom.core.model.enum.TimelineCategory
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkCarouselElementObjectEvent
import com.loom.core.network.model.NetworkCarouselElementObjectUser
import com.loom.core.network.model.NetworkObjectCarousel
import com.loom.core.network.model.NetworkObjectPost
import com.loom.core.network.model.NetworkObjectTitle
import com.loom.core.network.model.NetworkObjectsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.loom.core.database.model.PostEntity
import com.loom.core.database.model.UserEntity
import com.loom.core.database.model.EventEntity


class OfflineFirstTimelineRepository @Inject constructor(
    private val timelineDao: TimelineDao,
    private val timelineMetadataDao: TimelineMetadataDao,
    private val postDao: PostDao,
    private val carouselDao: CarouselDao,
    private val titleDao: TitleDao,
    private val userDao: UserDao,
    private val eventDao: EventDao,
    private val network: LoomNetworkDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : TimelineRepository {
    private var isFirstLoad = true

    override fun getObjects(timelineCategory: TimelineCategory) =
        timelineDao.getTimeline(timelineCategory.value)
            .map { it.map { entity -> entity.asExternalModel() } }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {

        val isAppInUse = !isFirstLoad

        return if (isAppInUse) {
            syncTimeline(TimelineCategory.FOR_YOU, forceRefresh = false)
        } else {
            syncTimeline(TimelineCategory.FOR_YOU, forceRefresh = true)
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
            val response = network.getTimeline(timelineCategory.value, currentCursor)

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
        val usersToSave = mutableListOf<UserEntity>()
        val eventsToSave = mutableListOf<EventEntity>()
        val carouselsToSave = mutableListOf<CarouselEntity>()
        val carouselItemsToSave = mutableListOf<CarouselItemEntity>()

        response.elements.forEach { netObj ->
            when (netObj) {
                is NetworkObjectPost -> {
                    postsToSave.add(netObj.asInternalPostEntity())
                }
                is NetworkObjectTitle -> {
                    titleDao.upsertTitle(netObj.id, netObj.text)
                }
                is NetworkObjectCarousel -> {
                    netObj.elements.forEach { element ->
                        when (element) {
                            is NetworkCarouselElementObjectUser -> {
                                val users = element.resource
                                usersToSave.addAll(
                                    users.map { it.asInternalUserEntity() }
                                )

                                users.forEach { netUser ->
                                    netUser.posts.let { netPosts ->
                                        postsToSave.addAll(
                                            netUser.posts.map { it.asInternalUserPostEntity() }
                                        )
                                    }
                                }
                            }
                            is NetworkCarouselElementObjectEvent -> {
                                eventsToSave.addAll(
                                    element.resource.map { it.asInternalEventEntity() }
                                )
                            }
                        }
                    }

                    carouselsToSave.add(netObj.asInternalCarouselEntity())
                    carouselItemsToSave.addAll(netObj.asInternalCarouselItemEntities())
                }
                else -> { throw IllegalArgumentException("Wrong element type: $netObj") }
            }
        }

        if (postsToSave.isNotEmpty()) postDao.upsertPosts(postsToSave)
        if (usersToSave.isNotEmpty()) userDao.upsertUsers(usersToSave)
        if (eventsToSave.isNotEmpty()) eventDao.upsertEvents(eventsToSave)
        if (carouselsToSave.isNotEmpty()) {
            carouselsToSave.forEachIndexed { index, carousel ->
                carouselDao.upsertCarousel(
                    carousel,
                    carouselItemsToSave.filter { it.carouselId == carousel.id }
                )
            }
        }


        val entities = response.elements.map {
            it.asInternalTimelineEntity(timelineCategory.value)
        }
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
*/