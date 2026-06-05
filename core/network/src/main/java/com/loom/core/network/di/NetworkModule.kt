package com.loom.core.network.di

import android.content.Context
import androidx.tracing.trace
import coil.ImageLoader
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.LoomNotificationService
import com.loom.core.network.retrofit.RetrofitLoomNetwork
import com.loom.core.network.websocket.OkHttpNotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import coil.decode.SvgDecoder
import com.loom.core.network.retrofit.AuthInterceptor
import com.loom.core.network.retrofit.TokenAuthenticator

import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true // Ayuda con nulos inesperados
        isLenient = true
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .pingInterval(30, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
        .build()

    @Provides
    @Singleton
    fun okHttpCallFactory(
        okHttpClient: OkHttpClient,
    ): Call.Factory = okHttpClient

    // Vinculación entre la interfaz a la versión de Retrofit (o Demo)
    @Provides
    @Singleton
    fun providesLoomNetworkDataSource(
        networkJson: Json,
        okhttpCallFactory: dagger.Lazy<Call.Factory>,
    ): LoomNetworkDataSource {
        return RetrofitLoomNetwork(networkJson, okhttpCallFactory)
    }

    @Provides
    @Singleton
    fun providesLoomNotificationService(
        okHttpClient: OkHttpClient,
    ): LoomNotificationService {
        return OkHttpNotificationService(okHttpClient)
    }

    @Provides
    @Singleton
    fun imageLoader(
        // We specifically request dagger.Lazy here, so that it's not instantiated from Dagger.
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext application: Context,
    ): ImageLoader = trace("LoomImageLoader") {
        ImageLoader.Builder(application)
            .callFactory { okHttpCallFactory.get() }
            .components { add(SvgDecoder.Factory()) }
            // Assume most content images are versioned urls
            // but some problematic images are fetching each time
            .respectCacheHeaders(false)
            .build()
    }
}