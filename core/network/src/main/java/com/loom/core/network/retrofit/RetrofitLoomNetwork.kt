package com.loom.core.network.retrofit

import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkPost
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton
import com.loom.core.network.BuildConfig
import com.loom.core.network.model.NetworkLoomResponse
import com.loom.core.network.model.NetworkTimelineResponse
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz interna de Retrofit para definir los endpoints.
 */
private interface RetrofitLoomNetworkApi {
    @GET(value = "posts/")
    suspend fun getPosts(): List<NetworkPost>

    // NUEVO: Endpoint para cualquier timeline con soporte de cursor
    @GET(value = "timeline/{type}")
    suspend fun getTimeline(
        @Path("type") type: String,
        @Query("cursor") cursor: String?
    ): NetworkLoomResponse
}


private const val LOOM_BASE_URL = BuildConfig.BACKEND_URL

@Singleton
internal class RetrofitLoomNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : LoomNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(LOOM_BASE_URL)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitLoomNetworkApi::class.java) // 1. Cambiado .class por .java

    override suspend fun getPosts(): List<NetworkPost> = networkApi.getPosts()

    override suspend fun getTimeline(
        timelineCategory: String,
        cursor: String?
    ): NetworkTimelineResponse {
        val result = networkApi.getTimeline(timelineCategory, cursor)
        return result.response.timeline
    }
}