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

/**
 * Interfaz interna de Retrofit para definir los endpoints.
 */
private interface RetrofitLoomNetworkApi {
    @GET(value = "posts/")
    suspend fun getPosts(): List<NetworkPost>
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
}