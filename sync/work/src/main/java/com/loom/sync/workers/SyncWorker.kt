package com.loom.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.loom.core.common.network.Dispatcher
import com.loom.core.common.network.LoomDispatchers.IO
import com.loom.core.data.Synchronizer
//import com.loom.core.data.repository.PostRepository
import com.loom.core.data.repository.TimelineRepository

import com.loom.sync.initializers.syncForegroundInfo
import com.loom.sync.status.SyncSubscriber
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import androidx.tracing.traceAsync
import com.loom.core.data.repository.ExploreRepository
import com.loom.core.data.repository.UserDataRepository
import com.loom.sync.initializers.SyncConstraints
import kotlinx.coroutines.flow.first
import com.loom.core.model.data.isLoggedIn



/**
 * Syncs the data layer by delegating to the appropriate repository instances with
 * sync functionality.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    //private val postRepository: PostRepository,
    private val timelineRepository: TimelineRepository,
    private val exploreRepository: ExploreRepository,
    private val userDataRepository: UserDataRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,

    private val syncSubscriber: SyncSubscriber,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        // 2. Comprobamos el estado del usuario antes de empezar
        val userData = userDataRepository.userData.first()
        if (!userData.isLoggedIn) {
            // Si no está logueado, terminamos con éxito pero sin hacer nada.
            // No usamos retry porque no queremos que WorkManager siga intentándolo.
            return@withContext Result.success()
        }

        traceAsync("Sync", 0) {


            syncSubscriber.subscribe()

            // First sync the repositories in parallel
            val syncedSuccessfully = awaitAll(
                async {
                    //postRepository.sync()
                    timelineRepository.sync()
                    //exploreRepository.sync()
                      },
            ).all { it }



            if (syncedSuccessfully) {
                Result.success()
            } else {
                Result.retry()
            }
        }
    }

    //override suspend fun getChangeListVersions(): ChangeListVersions = loomPreferences.getChangeListVersions()

    //override suspend fun updateChangeListVersions(update: ChangeListVersions.() -> ChangeListVersions, ) = loomPreferences.updateChangeListVersion(update)

    companion object {
        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
