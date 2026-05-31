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
import com.loom.core.network.model.NetworkAuthResponse
import com.loom.core.network.model.NetworkMediaResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import com.loom.core.network.model.NetworkExploreResponse
import com.loom.core.network.model.NetworkLoginRequest
import com.loom.core.network.model.NetworkLogoutResponse
import com.loom.core.network.model.NetworkObjectsResponse
import com.loom.core.network.model.NetworkPostsFeedResponse
import com.loom.core.network.model.NetworkPostsFeedEnvelope
import com.loom.core.network.model.NetworkRefreshRequest
import com.loom.core.network.model.NetworkRegisterRequest
import com.loom.core.network.model.NetworkTimelineResponse
import com.loom.core.network.model.NetworkTokenResponse
import com.loom.core.network.model.NetworkUserProfile
import com.loom.core.network.model.NetworkValidateEmailRequest
import com.loom.core.network.model.NetworkValidateEmailResponse
import com.loom.core.network.model.NetworkPostCreateRequest
import com.loom.core.network.model.NetworkPostFeedItem
import com.loom.core.network.model.NetworkFollowingUsersFeedEnvelope
import com.loom.core.network.model.NetworkFollowingUsersFeedResponse
import com.loom.core.network.model.NetworkFeedObjectEnvelope
import com.loom.core.network.model.NetworkFeedObjectResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz interna de Retrofit para definir los endpoints.
 */
private interface RetrofitLoomNetworkApi {
    @POST(value = "auth/login/")
    suspend fun login(
        @Body request: NetworkLoginRequest
    ): NetworkAuthResponse

    @POST(value = "auth/registration/")
    suspend fun register(
        @Body request: NetworkRegisterRequest
    ): NetworkAuthResponse

    @POST(value = "auth/logout/")
    suspend fun logout(): NetworkLogoutResponse

    @POST(value = "auth/token/refresh/")
    suspend fun refreshToken(
        @Body request: NetworkRefreshRequest
    ): NetworkTokenResponse

    @GET(value = "posts/")
    suspend fun getPosts(): List<NetworkPost>

    @POST(value = "posts/")
    suspend fun createPost(
        @Body request: NetworkPostCreateRequest
    ): NetworkPostFeedItem

    @GET(value = "timeline/{type}")
    suspend fun getTimeline(
        @Path("type") type: String,
        @Query("cursor") cursor: String?
    ): NetworkTimelineResponse

    @GET(value = "explore/")
    suspend fun getExplore(
        @Query("cursor") cursor: String?
    ): NetworkExploreResponse

    @POST(value = "auth/email/validate/")
    suspend fun validateEmail(
        @Body request: NetworkValidateEmailRequest
    ): NetworkValidateEmailResponse


    @GET(value = "posts/recommend/")
    suspend fun getPostsFeedForYou(
        @Query("cursor") cursor: String?
    ): NetworkPostsFeedEnvelope

    @GET(value = "feeds/for-you/")
    suspend fun getFeedObjectsForYou(
        @Query("cursor") cursor: String?
    ): NetworkFeedObjectEnvelope

    @GET(value = "posts/following/")
    suspend fun getPostsFeedFollowing(
        @Query("cursor") cursor: String?
    ): NetworkPostsFeedEnvelope

    @GET(value = "posts/tags/")
    suspend fun getPostsFeedTags(
        @Query("cursor") cursor: String?
    ): NetworkPostsFeedEnvelope

    @GET(value = "posts/me/")
    suspend fun getPostsFeedMe(
        @Query("cursor") cursor: String?
    ): NetworkPostsFeedEnvelope

    @GET(value = "posts/liked/")
    suspend fun getPostsFeedLiked(
        @Query("cursor") cursor: String?
    ): NetworkPostsFeedEnvelope

    @GET(value = "relationships/following/")
    suspend fun getFollowingUsers(
        @Query("cursor") cursor: String?
    ): NetworkFollowingUsersFeedEnvelope

    @GET(value = "profiles/profiles/{id}/")
    suspend fun getUserProfile(
        @Path("id") id: String
    ): NetworkUserProfile

    @GET(value = "profiles/profiles/me/")
    suspend fun getMyProfile(): NetworkUserProfile

    @Multipart
    @PATCH(value = "profiles/profiles/{id}/")
    suspend fun updateUserProfile(
        @Path("id") id: String,
        @Part displayName: MultipartBody.Part? = null,
        @Part bio: MultipartBody.Part? = null,
        @Part city: MultipartBody.Part? = null,
        @Part timezone: MultipartBody.Part? = null,
        @Part canBeFollowed: MultipartBody.Part? = null,
        @Part latitude: MultipartBody.Part? = null,
        @Part longitude: MultipartBody.Part? = null,
        @Part avatar: MultipartBody.Part? = null,
        @Part banner: MultipartBody.Part? = null
    ): NetworkUserProfile

    @Multipart
    @POST(value = "assets/media/")
    suspend fun uploadMedia(
        @Part file: MultipartBody.Part
    ): NetworkMediaResponse
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

    override suspend fun login(request: NetworkLoginRequest): NetworkAuthResponse =
        networkApi.login(request)

    override suspend fun register(
        request: NetworkRegisterRequest
    ): NetworkAuthResponse =
        networkApi.register(request)

    override suspend fun logout(): NetworkLogoutResponse =
        networkApi.logout()

    override suspend fun refreshToken(refreshToken: String): NetworkTokenResponse =
        networkApi.refreshToken(NetworkRefreshRequest(refreshToken))


    override suspend fun getPosts(): List<NetworkPost> = networkApi.getPosts()

    override suspend fun createPost(request: NetworkPostCreateRequest): NetworkPostFeedItem =
        networkApi.createPost(request)

    override suspend fun getExplore(
        cursor: String?
    ): NetworkObjectsResponse {
        val result = networkApi.getExplore( cursor)
        return result.response.explore
    }

    override suspend fun getTimeline(
        timelineCategory: String,
        cursor: String?
    ): NetworkObjectsResponse {
        val result = networkApi.getTimeline(timelineCategory, cursor)
        return result.response.timeline
    }

    override suspend fun validateEmail(
        email: String
    ): NetworkValidateEmailResponse {
        return networkApi.validateEmail(
            NetworkValidateEmailRequest(email)
        )
    }


    override suspend fun getPostsFeedForYou(cursor: String?): NetworkPostsFeedResponse {
        val result = networkApi.getPostsFeedForYou(cursor)
        return NetworkPostsFeedResponse(
            next = result.response.feed.queryParams?.cursor,
            previous = null,
            results = result.response.feed.elements
        )
    }

    override suspend fun getFeedObjectsForYou(cursor: String?): NetworkFeedObjectResponse {
        val result = networkApi.getFeedObjectsForYou(cursor)
        return NetworkFeedObjectResponse(
            next = result.response.feed.queryParams?.cursor,
            results = result.response.feed.elements
        )
    }

    override suspend fun getPostsFeedFollowing(cursor: String?): NetworkPostsFeedResponse {
        val result = networkApi.getPostsFeedFollowing(cursor)
        return NetworkPostsFeedResponse(
            next = result.response.feed.queryParams?.cursor,
            previous = null,
            results = result.response.feed.elements
        )
    }

    override suspend fun getPostsFeedTags(cursor: String?): NetworkPostsFeedResponse {
        val result = networkApi.getPostsFeedTags(cursor)
        return NetworkPostsFeedResponse(
            next = result.response.feed.queryParams?.cursor,
            previous = null,
            results = result.response.feed.elements
        )
    }

    override suspend fun getPostsFeedMe(cursor: String?): NetworkPostsFeedResponse {
        val result = networkApi.getPostsFeedMe(cursor)
        return NetworkPostsFeedResponse(
            next = result.response.feed.queryParams?.cursor,
            previous = null,
            results = result.response.feed.elements
        )
    }

    override suspend fun getPostsFeedLiked(cursor: String?): NetworkPostsFeedResponse {
        val result = networkApi.getPostsFeedLiked(cursor)
        return NetworkPostsFeedResponse(
            next = result.response.feed.queryParams?.cursor,
            previous = null,
            results = result.response.feed.elements
        )
    }

    override suspend fun getFollowingUsers(cursor: String?): NetworkFollowingUsersFeedResponse {
        val result = networkApi.getFollowingUsers(cursor)
        return NetworkFollowingUsersFeedResponse(
            next = result.response.feed.queryParams?.cursor,
            results = result.response.feed.elements
        )
    }

    override suspend fun getUserProfile(id: String): NetworkUserProfile =
        networkApi.getUserProfile(id)

    override suspend fun getMyProfile(): NetworkUserProfile =
        networkApi.getMyProfile()

    override suspend fun updateUserProfile(
        id: String,
        displayName: String?,
        bio: String?,
        city: String?,
        timezone: String?,
        canBeFollowed: Boolean?,
        latitude: Double?,
        longitude: Double?,
        avatar: MultipartBody.Part?,
        banner: MultipartBody.Part?
    ): NetworkUserProfile = networkApi.updateUserProfile(
        id = id,
        displayName = displayName?.let { MultipartBody.Part.createFormData("display_name", it) },
        bio = bio?.let { MultipartBody.Part.createFormData("bio", it) },
        city = city?.let { MultipartBody.Part.createFormData("city", it) },
        timezone = timezone?.let { MultipartBody.Part.createFormData("timezone", it) },
        canBeFollowed = canBeFollowed?.let {
            MultipartBody.Part.createFormData(
                "can_be_followed",
                it.toString()
            )
        },
        latitude = latitude?.let { MultipartBody.Part.createFormData("latitude", it.toString()) },
        longitude = longitude?.let { MultipartBody.Part.createFormData("longitude", it.toString()) },
        avatar = avatar,
        banner = banner
    )

    override suspend fun uploadMedia(file: MultipartBody.Part): NetworkMediaResponse =
        networkApi.uploadMedia(file)

}
