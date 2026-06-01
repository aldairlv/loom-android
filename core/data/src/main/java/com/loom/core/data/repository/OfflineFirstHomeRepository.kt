package com.loom.core.data.repository

import android.util.Log
import com.loom.core.model.data.FeedObject
import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostInteractions
import com.loom.core.model.data.PostStats
import com.loom.core.model.data.PostMedia
import com.loom.core.model.data.PostParent
import com.loom.core.model.data.PostsFeedResult
import com.loom.core.model.data.RowModel
import com.loom.core.model.data.TextBlock
import com.loom.core.model.data.ImageBlock
import com.loom.core.model.data.VideoBlock
import com.loom.core.model.data.LayoutRoot
import com.loom.core.model.data.LayoutRow
import com.loom.core.database.dao.PostDao
import com.loom.core.database.dao.TimelineDao
import com.loom.core.database.model.TimelineEntity
import com.loom.core.database.model.asEntity
import com.loom.core.database.model.asFeedObjectModel
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkContentInput
import com.loom.core.network.model.NetworkLayoutRoot
import com.loom.core.network.model.NetworkLayoutRow
import com.loom.core.network.model.NetworkObject
import com.loom.core.network.model.NetworkObjectPost
import com.loom.core.network.model.NetworkPostAuthor
import com.loom.core.network.model.NetworkPostInteractions
import com.loom.core.network.model.NetworkPostStats
import com.loom.core.network.model.NetworkPostContent
import com.loom.core.network.model.NetworkPostCreateRequest
import com.loom.core.network.model.NetworkPostFeedItem
import com.loom.core.network.model.NetworkPostMedia
import com.loom.core.network.model.NetworkPostParent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import com.loom.core.network.model.NetworkMediaResponse
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

internal class OfflineFirstHomeRepository @Inject constructor(
    private val network: LoomNetworkDataSource,
    private val postDao: PostDao,
    private val timelineDao: TimelineDao,
) : HomeRepository {

    override fun getFeedObjectsForYouFlow(): Flow<List<FeedObject>> {
        return timelineDao.getTimeline("for_you").map { populatedObjects ->
            populatedObjects.mapNotNull { it.asFeedObjectModel() }
        }
    }

    override suspend fun getPostsFeedForYou(cursor: String?): PostsFeedResult {
        val networkResponse = network.getPostsFeedForYou(cursor)
        return PostsFeedResult(
            posts = networkResponse.results.map { it.asExternalModel() },
            nextCursor = networkResponse.next
        )
    }

    override suspend fun getFeedObjectsForYou(cursor: String?, isRefresh: Boolean): FeedObjectsResult {
        if (isRefresh) {
            timelineDao.clearTimelineObjects("for_you")
        }

        val networkResponse = network.getFeedObjectsForYou(cursor)
        val feedObjects = networkResponse.results.mapNotNull { it.asExternalModel() }

        Log.d("LOOM_DATA_FLOW", "Repository: Mapped ${feedObjects.size} objects. Checking for Root content...")
        feedObjects.forEachIndexed { index, feedObject ->
            if (feedObject is FeedObject.PostFeedObject) {
                val post = feedObject.post
                Log.d("LOOM_DATA_FLOW", "Repository Post [$index]: id=${post.id}, rootId=${post.root?.id}, rootContentSize=${post.root?.contents?.size ?: 0}")
            }
        }

        // Save to DB
        val postEntities = feedObjects.filterIsInstance<FeedObject.PostFeedObject>().map { it.post.asEntity() }
        postDao.upsertPosts(postEntities)

        val timelineEntities = networkResponse.results.map { networkObj ->
            TimelineEntity(
                timelineCategory = "for_you",
                objectType = networkObj.objectType,
                objectId = networkObj.id,
                streamGlobalPosition = networkObj.streamGlobalPosition,
                streamSessionId = networkObj.streamSessionId
            )
        }
        timelineDao.upsertTimelineObjects(timelineEntities)

        return FeedObjectsResult(
            objects = feedObjects,
            nextCursor = networkResponse.next
        )
    }

    override suspend fun getPostsFeedFollowing(cursor: String?): PostsFeedResult {
        Log.d("LOOM_DEBUG", "Repository: Calling network.getPostsFeedFollowing(cursor=$cursor)")
        val networkResponse = network.getPostsFeedFollowing(cursor)
        Log.d("LOOM_DEBUG", "Repository: Network response received. Results count: ${networkResponse.results.size}")
        
        return try {
            val posts = networkResponse.results.map { it.asExternalModel() }
            Log.d("LOOM_DEBUG", "Repository: Mapping successful")
            PostsFeedResult(
                posts = posts,
                nextCursor = networkResponse.next
            )
        } catch (e: Exception) {
            Log.e("LOOM_DEBUG", "Repository: Error mapping NetworkPostFeedItem to PostFeedItem", e)
            throw e
        }
    }

    override suspend fun getPostsFeedTags(cursor: String?): PostsFeedResult {
        val networkResponse = network.getPostsFeedTags(cursor)
        return PostsFeedResult(
            posts = networkResponse.results.map { it.asExternalModel() },
            nextCursor = networkResponse.next
        )
    }

    override suspend fun uploadMedia(
        fileName: String,
        mimeType: String,
        fileBytes: ByteArray
    ): PostMedia {
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val finalFileName = if (extension != null && !fileName.endsWith(".$extension", ignoreCase = true)) {
            "$fileName.$extension"
        } else {
            fileName
        }
        val requestFile = fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", finalFileName, requestFile)
        val response = network.uploadMedia(body)
        return response.asExternalModel()
    }

    override suspend fun createPost(
        status: String,
        tags: List<String>,
        rows: List<RowModel>,
        parentId: String?,
        rootId: String?
    ) {
        val contentsInput = mutableListOf<NetworkContentInput>()
        val layoutDisplay = mutableListOf<NetworkLayoutRow>()
        var currentIndex = 0

        rows.filter { row ->
            !row.id.startsWith("sensor") && row.blocks.isNotEmpty()
        }.forEach { row ->
            val rowIndices = mutableListOf<Int>()
            row.blocks.forEach { block ->
                val contentInput = when (block) {
                    is TextBlock -> NetworkContentInput(type = "text", text = block.text)
                    is ImageBlock -> NetworkContentInput(type = "image", mediaId = block.backendId)
                    is VideoBlock -> NetworkContentInput(type = "video", mediaId = block.backendId)
                }
                contentsInput.add(contentInput)
                rowIndices.add(currentIndex)
                currentIndex++
            }
            if (rowIndices.isNotEmpty()) {
                layoutDisplay.add(NetworkLayoutRow(blocks = rowIndices))
            }
        }

        val request = NetworkPostCreateRequest(
            parentId = parentId,
            rootId = rootId,
            status = status,
            tags = tags,
            contentsInput = contentsInput,
            layout = listOf(NetworkLayoutRoot(type = "rows", display = layoutDisplay)),
            showTrailing = true
        )

        network.createPost(request)
    }

    override suspend fun quickRepost(postId: String, parentId: String?, rootId: String?) {
        val request = NetworkPostCreateRequest(
            parentId = parentId ?: postId,
            rootId = rootId ?: postId,
            status = "published",
            tags = emptyList(),
            contentsInput = emptyList(),
            layout = emptyList(),
            showTrailing = true
        )
        network.createPost(request)
    }

    override suspend fun toggleLike(postId: String, isLiked: Boolean) {
        // Optimistic update
        val localPost = postDao.getPost(postId)
        if (localPost != null) {
            val currentInteractions = localPost.interactions ?: PostInteractions(false, false, false)
            val currentStats = localPost.stats ?: PostStats(0, 0, 0)
            
            val newInteractions = currentInteractions.copy(liked = !isLiked)
            val newStats = currentStats.copy(
                likesCount = if (isLiked) (currentStats.likesCount - 1).coerceAtLeast(0) 
                             else currentStats.likesCount + 1
            )
            
            postDao.upsertPost(localPost.copy(interactions = newInteractions, stats = newStats))
        }

        try {
            if (isLiked) {
                network.unlikePost(postId)
            } else {
                network.likePost(postId)
            }
        } catch (e: Exception) {
            // Rollback on error
            if (localPost != null) {
                postDao.upsertPost(localPost)
            }
            throw e
        }
    }

    override suspend fun toggleFollow(profileId: String, isFollowed: Boolean) {
        try {
            if (isFollowed) {
                network.unfollowUser(profileId)
            } else {
                network.followUser(profileId)
            }
            
            // After successful network call, update all posts from this author in the DB
            val allPosts = postDao.getAllPosts()
            val updatedPosts = allPosts.filter { it.author.id == profileId }.map { post ->
                post.copy(author = post.author.copy(isFollowed = !isFollowed))
            }
            if (updatedPosts.isNotEmpty()) {
                postDao.upsertPosts(updatedPosts)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}

private fun NetworkMediaResponse.asExternalModel() = PostMedia(
    id = id,
    url = url,
    type = type,
    width = width,
    height = height
)

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
    interactions = interactions?.asExternalModel(),
    stats = stats?.asExternalModel(),
    createdAt = created_at.toInstantOrNow(),
    updatedAt = updated_at.toInstantOrNow(),
    publishedAt = published_at?.toInstantOrNull()
)

private fun NetworkObject.asExternalModel(): FeedObject? {
    return when (this) {
        is NetworkObjectPost -> FeedObject.PostFeedObject(
            post = PostFeedItem(
                id = id,
                author = author.asExternalModel(),
                parent = parent?.asExternalModel(),
                root = root?.asExternalModel(),
                trail = trail.map { it.asExternalModel() },
                status = status,
                tags = tags,
                contents = contents.map { it.asExternalModel() },
                layout = layout.map { it.asExternalModel() },
                interactions = interactions?.asExternalModel(),
                stats = stats?.asExternalModel(),
                createdAt = created_at.toInstantOrNow(),
                updatedAt = updated_at.toInstantOrNow(),
                publishedAt = published_at?.toInstantOrNull()
            ),
            streamGlobalPosition = streamGlobalPosition,
            streamSessionId = streamSessionId
        )
        else -> null // Handle other types as they are implemented
    }
}

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

private fun NetworkPostAuthor.asExternalModel() = PostAuthor(
    id = id,
    displayName = display_name,
    avatarUrl = avatar_url ?: "",
    isFollowed = is_followed
)

private fun NetworkPostInteractions.asExternalModel() = PostInteractions(
    liked = liked,
    reposted = reposted,
    commented = commented
)

private fun NetworkPostStats.asExternalModel() = PostStats(
    likesCount = likes_count,
    repostsCount = reposts_count,
    commentsCount = comments_count
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
