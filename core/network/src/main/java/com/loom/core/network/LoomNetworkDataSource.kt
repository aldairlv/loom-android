package com.loom.core.network

import com.loom.core.network.model.NetworkAuthResponse
import com.loom.core.network.model.NetworkMediaResponse
import okhttp3.MultipartBody
import com.loom.core.network.model.NetworkLoginRequest
import com.loom.core.network.model.NetworkLogoutResponse
import com.loom.core.network.model.NetworkObjectsResponse
import com.loom.core.network.model.NetworkPost
import com.loom.core.network.model.NetworkPostsFeedResponse
import com.loom.core.network.model.NetworkRegisterRequest
import com.loom.core.network.model.NetworkTimelineResponse
import com.loom.core.network.model.NetworkTokenResponse
import com.loom.core.network.model.NetworkValidateEmailResponse
import com.loom.core.network.model.NetworkUserProfile
import com.loom.core.network.model.NetworkFollowingUsersFeedResponse
import com.loom.core.network.model.NetworkFeedObjectResponse
import com.loom.core.network.model.NetworkPostFeedItem
import com.loom.core.network.model.NetworkPostCreateRequest
import com.loom.core.network.model.NetworkPostResponse
import com.loom.core.network.model.NetworkLikeResponse
import com.loom.core.network.model.NetworkFollowResponse
import com.loom.core.network.model.NetworkComment
import com.loom.core.network.model.NetworkCommentResponse

interface LoomNetworkDataSource {
    suspend fun login(request: NetworkLoginRequest): NetworkAuthResponse

    suspend fun register(request: NetworkRegisterRequest): NetworkAuthResponse

    suspend fun logout(): NetworkLogoutResponse

    suspend fun refreshToken(refreshToken: String): NetworkTokenResponse

    suspend fun getPosts(): List<NetworkPost>

    suspend fun createPost(request: NetworkPostCreateRequest): NetworkPostFeedItem

    suspend fun getExplore(
        cursor: String? = null
    ): NetworkObjectsResponse

    suspend fun getTimeline(
        timelineCategory: String,
        cursor: String? = null
    ): NetworkObjectsResponse

    suspend fun validateEmail(
        email: String
    ): NetworkValidateEmailResponse


    suspend fun getPostsFeedForYou(
        cursor: String? = null
    ): NetworkPostsFeedResponse

    suspend fun getFeedObjectsForYou(
        cursor: String? = null
    ): NetworkFeedObjectResponse

    suspend fun getFeedEventsSoon(
        cursor: String? = null
    ): NetworkFeedObjectResponse

    suspend fun getPostsFeedFollowing(
        cursor: String? = null
    ): NetworkPostsFeedResponse

    suspend fun getPostsFeedTags(
        cursor: String? = null
    ): NetworkPostsFeedResponse

    suspend fun getPostsFeedMe(
        cursor: String? = null
    ): NetworkPostsFeedResponse

    suspend fun getPostsFeedLiked(
        cursor: String? = null
    ): NetworkPostsFeedResponse

    suspend fun getFollowingUsers(
        cursor: String? = null
    ): NetworkFollowingUsersFeedResponse

    suspend fun getUserProfile(
        id: String
    ): NetworkUserProfile

    suspend fun getMyProfile(): NetworkUserProfile

    suspend fun updateUserProfile(
        id: String,
        displayName: String? = null,
        bio: String? = null,
        city: String? = null,
        timezone: String? = null,
        canBeFollowed: Boolean? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        avatar: MultipartBody.Part? = null,
        banner: MultipartBody.Part? = null
    ): NetworkUserProfile

    suspend fun uploadMedia(
        file: MultipartBody.Part
    ): NetworkMediaResponse

    suspend fun likePost(id: String): NetworkLikeResponse
    suspend fun unlikePost(id: String)

    suspend fun followUser(profileId: String): NetworkFollowResponse
    suspend fun unfollowUser(profileId: String)

    suspend fun getComments(postId: String, cursor: String? = null): NetworkCommentResponse
    suspend fun createComment(postId: String, text: String): NetworkComment
    suspend fun createReply(postId: String, commentId: String, text: String): NetworkComment
    suspend fun deleteComment(postId: String, commentId: String)
}
