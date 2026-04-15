package com.loom.core.network.di

import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.retrofit.RetrofitLoomNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
        .build()

    // Vinculación entre la interfaz a la versión de Retrofit (o Demo)
    @Provides
    @Singleton
    fun providesLoomNetworkDataSource(
        networkJson: Json,
        okhttpCallFactory: dagger.Lazy<Call.Factory>,
    ): LoomNetworkDataSource {
        return RetrofitLoomNetwork(networkJson, okhttpCallFactory)
    }
}