package com.loom.core.network.demo

import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkPost
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

/*
class DemoLoomNetworkDataSource @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher, // Deberás tener tu módulo de Dispatchers
    private val networkJson: Json,
    private val assets: DemoAssetManager,
) : LoomNetworkDataSource {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPosts(): List<NetworkPost> =
        withContext(ioDispatcher) {
            assets.open("posts.json").use { networkJson.decodeFromStream(it) }
        }
}

 */