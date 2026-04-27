package com.loom.core.data.repository
/*
import com.loom.core.common.network.Dispatcher
import com.loom.core.common.network.LoomDispatchers.IO
import com.loom.core.data.Syncable
import com.loom.core.data.Synchronizer
import com.loom.core.data.model.asPostEntity
import com.loom.core.data.suspendRunCatching
import com.loom.core.database.dao.PostDao
import com.loom.core.database.model.asExternalModel
import com.loom.core.network.LoomNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


internal class OfflineFirstPostRepository @Inject constructor(
    private val postDao: PostDao,
    private val network: LoomNetworkDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : PostRepository {

    override fun getPosts() = postDao.getPostEntities()
        .map { it.map { entity -> entity.asExternalModel() } }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        suspendRunCatching {
            withContext(ioDispatcher) {
                val networkPosts = network.getPosts()
                postDao.upsertPosts(networkPosts.map { it.asPostEntity() })
            }
        }.isSuccess
}
*/