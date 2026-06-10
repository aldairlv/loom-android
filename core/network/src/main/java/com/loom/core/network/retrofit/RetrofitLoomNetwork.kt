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
import com.loom.core.network.model.NetworkLikeResponse
import com.loom.core.network.model.NetworkFollowResponse
import com.loom.core.network.model.NetworkComment
import com.loom.core.network.model.NetworkCommentRequest
import com.loom.core.network.model.NetworkCommentEnvelope
import com.loom.core.network.model.NetworkCommentResponse
import com.loom.core.network.model.NetworkEventDetailEnvelope
import com.loom.core.network.model.NetworkEventCreateRequest
import com.loom.core.network.model.NetworkEventCreateResponse
import com.loom.core.network.model.NetworkNotificationEnvelope
import com.loom.core.network.model.NetworkNotificationList
import com.loom.core.network.model.NetworkDeviceRequest
import com.loom.core.network.model.NetworkDeviceResponse
import com.loom.core.network.model.NetworkConversation
import com.loom.core.network.model.NetworkConversationEnvelope
import com.loom.core.network.model.NetworkConversationsEnvelope
import com.loom.core.network.model.NetworkMessage
import com.loom.core.network.model.NetworkMessagesEnvelope
import com.loom.core.network.model.NetworkMessageEnvelope
import com.loom.core.network.model.NetworkCreateDirectChatRequest
import com.loom.core.network.model.NetworkCreateGroupChatRequest
import com.loom.core.network.model.NetworkSendMessageRequest
import com.loom.core.network.model.NetworkUnreadEnvelope
import com.loom.core.network.model.NetworkReadEnvelope
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @GET(value = "feeds/soon/")
    suspend fun getFeedEventsSoon(
        @Query("cursor") cursor: String?
    ): NetworkFeedObjectEnvelope

    @GET(value = "events/events/{id}/")
    suspend fun getEvent(
        @Path("id") id: String
    ): NetworkEventDetailEnvelope

    @POST(value = "events/events/")
    suspend fun createEvent(
        @Body request: NetworkEventCreateRequest
    ): NetworkEventCreateResponse

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

    @POST(value = "posts/{id}/likes/")
    suspend fun likePost(
        @Path("id") id: String
    ): NetworkLikeResponse

    @DELETE(value = "posts/{id}/likes/")
    suspend fun unlikePost(
        @Path("id") id: String
    )

    @POST(value = "users/{id}/followers/")
    suspend fun followUser(
        @Path("id") id: String
    ): NetworkFollowResponse

    @DELETE(value = "users/{id}/followers/")
    suspend fun unfollowUser(
        @Path("id") id: String
    )

    @GET(value = "posts/{id}/comments/")
    suspend fun getComments(
        @Path("id") id: String,
        @Query("cursor") cursor: String?
    ): NetworkCommentEnvelope

    @POST(value = "posts/{id}/comments/")
    suspend fun createComment(
        @Path("id") id: String,
        @Body request: NetworkCommentRequest
    ): NetworkComment

    @POST(value = "posts/{id}/comments/{comment_id}/")
    suspend fun createReply(
        @Path("id") id: String,
        @Path("comment_id") commentId: String,
        @Body request: NetworkCommentRequest
    ): NetworkComment

    @DELETE(value = "posts/{id}/comments/{comment_id}/")
    suspend fun deleteComment(
        @Path("id") id: String,
        @Path("comment_id") commentId: String
    )

    @GET(value = "notifications/")
    suspend fun getNotifications(
        @Query("cursor") cursor: String?
    ): NetworkNotificationEnvelope

    @POST(value = "devices/")
    suspend fun registerDevice(
        @Body request: NetworkDeviceRequest
    ): NetworkDeviceResponse

    // Chats
    @GET(value = "chats/conversations/")
    suspend fun getConversations(): NetworkConversationsEnvelope

    @POST(value = "chats/conversations/")
    suspend fun createDirectChat(
        @Body request: NetworkCreateDirectChatRequest
    ): NetworkConversationEnvelope

    @POST(value = "chats/conversations/group/")
    suspend fun createGroupChat(
        @Body request: NetworkCreateGroupChatRequest
    ): NetworkConversationEnvelope

    @GET(value = "chats/conversations/{id}/")
    suspend fun getConversation(
        @Path("id") id: String
    ): NetworkConversationEnvelope

    @GET(value = "chats/conversations/{id}/messages/")
    suspend fun getMessages(
        @Path("id") id: String,
        @Query("before") before: String? = null,
        @Query("limit") limit: Int = 50
    ): NetworkMessagesEnvelope

    @POST(value = "chats/conversations/{id}/messages/")
    suspend fun sendMessage(
        @Path("id") id: String,
        @Body request: NetworkSendMessageRequest
    ): NetworkMessageEnvelope

    @POST(value = "chats/conversations/{id}/read/")
    suspend fun markRead(
        @Path("id") id: String
    ): NetworkReadEnvelope

    @GET(value = "chats/conversations/{id}/unread/")
    suspend fun getUnreadCount(
        @Path("id") id: String
    ): NetworkUnreadEnvelope

    @DELETE(value = "chats/messages/{id}/")
    suspend fun deleteMessage(
        @Path("id") id: String
    )
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
        android.util.Log.d("LOOM_DATA_FLOW", "Network Response (ForYou): Found ${result.response.feed.elements.size} elements")
        result.response.feed.elements.forEachIndexed { index, networkObject ->
            if (networkObject is com.loom.core.network.model.NetworkObjectPost) {
                android.util.Log.d("LOOM_DATA_FLOW", "Network Post [$index]: id=${networkObject.id}, hasRoot=${networkObject.root != null}, rootContentSize=${networkObject.root?.contents?.size}")
            }
        }
        return NetworkFeedObjectResponse(
            next = result.response.feed.queryParams?.cursor,
            results = result.response.feed.elements
        )
    }

    override suspend fun getFeedEventsSoon(cursor: String?): NetworkFeedObjectResponse {
        val result = networkApi.getFeedEventsSoon(cursor)
        return NetworkFeedObjectResponse(
            next = result.response.feed.queryParams?.cursor,
            results = result.response.feed.elements
        )
    }

    override suspend fun getEvent(id: String): NetworkFeedObjectResponse {
        android.util.Log.d("LOOM_EVENT_DETAIL", "Network: Fetching event details for id: $id")
        return try {
            val result = networkApi.getEvent(id)
            android.util.Log.d("LOOM_EVENT_DETAIL", "Network: Successfully fetched event: ${result.response.id}")
            NetworkFeedObjectResponse(
                next = null,
                results = listOf(result.response)
            )
        } catch (e: Exception) {
            android.util.Log.e("LOOM_EVENT_DETAIL", "Network: Error fetching event details for id: $id", e)
            throw e
        }
    }

    override suspend fun createEvent(request: NetworkEventCreateRequest): NetworkEventCreateResponse =
        networkApi.createEvent(request)

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

    override suspend fun likePost(id: String): NetworkLikeResponse =
        networkApi.likePost(id)

    override suspend fun unlikePost(id: String) =
        networkApi.unlikePost(id)

    override suspend fun followUser(profileId: String): NetworkFollowResponse =
        networkApi.followUser(profileId)

    override suspend fun unfollowUser(profileId: String) =
        networkApi.unfollowUser(profileId)

    override suspend fun getComments(postId: String, cursor: String?): NetworkCommentResponse {
        val result = networkApi.getComments(postId, cursor)
        return NetworkCommentResponse(
            next = result.response.comments.queryParams?.cursor,
            results = result.response.comments.elements
        )
    }

    override suspend fun createComment(postId: String, text: String): NetworkComment =
        networkApi.createComment(postId, NetworkCommentRequest(text))

    override suspend fun createReply(postId: String, commentId: String, text: String): NetworkComment =
        networkApi.createReply(postId, commentId, NetworkCommentRequest(text))

    override suspend fun deleteComment(postId: String, commentId: String) =
        networkApi.deleteComment(postId, commentId)

    override suspend fun getNotifications(cursor: String?): NetworkNotificationList {
        val result = networkApi.getNotifications(cursor)
        return result.response.notifications
    }

    override suspend fun registerDevice(request: NetworkDeviceRequest): NetworkDeviceResponse =
        networkApi.registerDevice(request)

    override suspend fun getConversations(): List<NetworkConversation> =
        networkApi.getConversations().response.conversations.elements

    override suspend fun createDirectChat(userId: String): NetworkConversation =
        networkApi.createDirectChat(NetworkCreateDirectChatRequest(userId)).response.conversation

    override suspend fun createGroupChat(name: String, participantIds: List<String>): NetworkConversation =
        networkApi.createGroupChat(NetworkCreateGroupChatRequest(name, participantIds)).response.conversation

    override suspend fun getConversation(id: String): NetworkConversation =
        networkApi.getConversation(id).response.conversation

    override suspend fun getMessages(
        conversationId: String,
        before: String?,
        limit: Int
    ): List<NetworkMessage> =
        networkApi.getMessages(conversationId, before, limit).response.messages.elements

    override suspend fun sendMessage(
        conversationId: String,
        content: String?,
        type: String,
        mediaUrl: String?
    ): NetworkMessage =
        networkApi.sendMessage(
            conversationId,
            NetworkSendMessageRequest(content, type, mediaUrl)
        ).response.message

    override suspend fun markRead(conversationId: String) {
        networkApi.markRead(conversationId)
    }

    override suspend fun getUnreadCount(conversationId: String): Int =
        networkApi.getUnreadCount(conversationId).response.unread.count

    override suspend fun deleteMessage(messageId: String) {
        networkApi.deleteMessage(messageId)
    }

}
