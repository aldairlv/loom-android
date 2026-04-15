package com.loom.core.data

import android.util.Log
import kotlin.coroutines.cancellation.CancellationException

data class ChangeListVersions(val postVersion: Int = 0)

interface Synchronizer {
    suspend fun getChangeListVersions(): ChangeListVersions
    suspend fun updateChangeListVersions(update: ChangeListVersions.() -> ChangeListVersions)
    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (c: CancellationException) {
    throw c
} catch (e: Exception) {
    Log.i("suspendRunCatching", "Failed to evaluate block", e)
    Result.failure(e)
}