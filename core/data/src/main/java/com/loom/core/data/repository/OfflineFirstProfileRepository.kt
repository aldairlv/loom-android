package com.loom.core.data.repository

import com.loom.core.common.result.Result
import com.loom.core.model.data.FollowingUserFeedItem
import com.loom.core.model.data.FollowingUsersResult
import com.loom.core.model.data.LayoutRoot
import com.loom.core.model.data.LayoutRow
import com.loom.core.model.data.LocationCoords
import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostMedia
import com.loom.core.model.data.PostParent
import com.loom.core.model.data.PostsFeedResult
import com.loom.core.model.data.UserAccountProfile
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkLayoutRoot
import com.loom.core.network.model.NetworkLayoutRow
import com.loom.core.network.model.NetworkFollowingUserItem
import com.loom.core.network.model.NetworkLocationCoords
import com.loom.core.network.model.NetworkPostAuthor
import com.loom.core.network.model.NetworkPostContent
import com.loom.core.network.model.NetworkPostFeedItem
import com.loom.core.network.model.NetworkPostMedia
import com.loom.core.network.model.NetworkPostParent
import com.loom.core.network.model.NetworkUserProfile
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import retrofit2.HttpException
import javax.inject.Inject

internal class OfflineFirstProfileRepository @Inject constructor(
    private val network: LoomNetworkDataSource,
) : ProfileRepository {
    override suspend fun getMyProfile(): Result<UserAccountProfile> {
        return try {
            val networkResponse = network.getMyProfile()
            Result.Success(networkResponse.asExternalModel())
        } catch (e: Exception) {
            val message = when (e) {
                is HttpException -> {
                    val errorBody = try {
                        e.response()?.errorBody()?.string() ?: ""
                    } catch (ex: Exception) {
                        ""
                    }
                    "Error ${e.code()}: $errorBody"
                }
                else -> e.message ?: "Unknown error"
            }
            Result.Error(Exception(message, e))
        }
    }

    override suspend fun getMyPosts(cursor: String?): Result<PostsFeedResult> {
        return try {
            val networkResponse = network.getPostsFeedMe(cursor)
            Result.Success(
                PostsFeedResult(
                    posts = networkResponse.results.map { it.asExternalModel() },
                    nextCursor = networkResponse.next
                )
            )
        } catch (e: Exception) {
            val message = when (e) {
                is HttpException -> {
                    val errorBody = try {
                        e.response()?.errorBody()?.string() ?: ""
                    } catch (ex: Exception) {
                        ""
                    }
                    "Error ${e.code()}: $errorBody"
                }
                else -> e.message ?: "Unknown error"
            }
            Result.Error(Exception(message, e))
        }
    }

    override suspend fun getLikedPosts(cursor: String?): Result<PostsFeedResult> {
        return try {
            val networkResponse = network.getPostsFeedLiked(cursor)
            Result.Success(
                PostsFeedResult(
                    posts = networkResponse.results.map { it.asExternalModel() },
                    nextCursor = networkResponse.next
                )
            )
        } catch (e: Exception) {
            val message = when (e) {
                is HttpException -> {
                    val errorBody = try {
                        e.response()?.errorBody()?.string() ?: ""
                    } catch (ex: Exception) {
                        ""
                    }
                    "Error ${e.code()}: $errorBody"
                }
                else -> e.message ?: "Unknown error"
            }
            Result.Error(Exception(message, e))
        }
    }

    override suspend fun getFollowingUsers(cursor: String?): Result<FollowingUsersResult> {
        return try {
            val networkResponse = network.getFollowingUsers(cursor)
            Result.Success(
                FollowingUsersResult(
                    users = networkResponse.results.map { it.asExternalModel() },
                    nextCursor = networkResponse.next
                )
            )
        } catch (e: Exception) {
            val message = when (e) {
                is HttpException -> {
                    val errorBody = try {
                        e.response()?.errorBody()?.string() ?: ""
                    } catch (ex: Exception) {
                        ""
                    }
                    "Error ${e.code()}: $errorBody"
                }
                else -> e.message ?: "Unknown error"
            }
            Result.Error(Exception(message, e))
        }
    }

}

private fun NetworkPostFeedItem.asExternalModel(): PostFeedItem = PostFeedItem(
    id = id,
    author = author.asExternalModel(),
    parent = parent?.asExternalModel(),
    root = root?.asExternalModel(),
    trail = trail.map { it.asExternalModel() },
    status = status,
    tags = tags,
    contents = contents.map { it.asExternalModel() },
    layout = layout.map { it.asExternalModel() },
    createdAt = created_at.toInstantOrNow(),
    updatedAt = updated_at.toInstantOrNow(),
    publishedAt = published_at?.toInstantOrNull()
)

private fun String.toInstantOrNow(): Instant {
    return try {
        if (this.isBlank()) Clock.System.now() else Instant.parse(this)
    } catch (e: Exception) {
        Clock.System.now()
    }
}

private fun String.toInstantOrNull(): Instant? {
    return try {
        if (this.isBlank()) null else Instant.parse(this)
    } catch (e: Exception) {
        null
    }
}

private fun NetworkFollowingUserItem.asExternalModel() = FollowingUserFeedItem(
    id = id,
    username = username,
    displayName = displayName,
    avatarUrl = avatar,
    last_posted_at = lastPostedAt?.toInstantOrNull()
)

private fun NetworkUserProfile.asExternalModel() = UserAccountProfile(
    id = id,
    user = user,
    username = username,
    displayName = display_name,
    bio = bio,
    city = city,
    timezone = timezone,
    canBeFollowed = can_be_followed,
    avatarUrl = avatar_url,
    bannerUrl = banner_url,
    locationCoords = location_coords?.asExternalModel()
)

private fun NetworkLocationCoords.asExternalModel() = LocationCoords(
    latitude = latitude,
    longitude = longitude
)

private fun NetworkPostAuthor.asExternalModel() = PostAuthor(
    id = id,
    displayName = display_name,
    avatarUrl = avatar_url ?: ""
)

private fun NetworkPostParent.asExternalModel() = PostParent(
    id = id,
    author = author.asExternalModel(),
    contents = contents?.map { it.asExternalModel() },
    layout = layout?.map { it.asExternalModel() }
)

private fun NetworkPostContent.asExternalModel() = PostFeedContent(
    id = id,
    type = type,
    order = order,
    text = text,
    media = media?.asExternalModel()
)

private fun NetworkPostMedia.asExternalModel() = PostMedia(
    id = id,
    url = url,
    type = type,
    width = width ?: 0,
    height = height ?: 0
)

private fun NetworkLayoutRoot.asExternalModel() = LayoutRoot(
    type = type,
    display = display.map { it.asExternalModel() }
)

private fun NetworkLayoutRow.asExternalModel() = LayoutRow(
    blocks = blocks
)
