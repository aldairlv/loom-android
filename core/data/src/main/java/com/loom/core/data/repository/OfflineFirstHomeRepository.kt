package com.loom.core.data.repository

import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostMedia
import com.loom.core.model.data.PostParent
import com.loom.core.model.data.PostsFeedResult
import com.loom.core.network.LoomNetworkDataSource
import com.loom.core.network.model.NetworkPostAuthor
import com.loom.core.network.model.NetworkPostContent
import com.loom.core.network.model.NetworkPostFeedItem
import com.loom.core.network.model.NetworkPostMedia
import com.loom.core.network.model.NetworkPostParent
import kotlinx.datetime.Instant
import javax.inject.Inject

internal class OfflineFirstHomeRepository @Inject constructor(
    private val network: LoomNetworkDataSource,
) : HomeRepository {

    override suspend fun getPostsFeedForYou(cursor: String?): PostsFeedResult {
        val networkResponse = network.getPostsFeedForYou(cursor)
        return PostsFeedResult(
            posts = networkResponse.results.map { it.asExternalModel() },
            nextCursor = networkResponse.next
        )
    }

    override suspend fun getPostsFeedFollowing(cursor: String?): PostsFeedResult {
        val networkResponse = network.getPostsFeedFollowing(cursor)
        return PostsFeedResult(
            posts = networkResponse.results.map { it.asExternalModel() },
            nextCursor = networkResponse.next
        )
    }

    override suspend fun getPostsFeedTags(cursor: String?): PostsFeedResult {
        val networkResponse = network.getPostsFeedTags(cursor)
        return PostsFeedResult(
            posts = networkResponse.results.map { it.asExternalModel() },
            nextCursor = networkResponse.next
        )
    }
}

private fun NetworkPostFeedItem.asExternalModel() = PostFeedItem(
    id = id,
    author = author.asExternalModel(),
    parent = parent?.asExternalModel(),
    root = root?.asExternalModel(),
    status = status,
    tags = tags,
    contents = contents.map { it.asExternalModel() },
    createdAt = Instant.parse(created_at),
    updatedAt = Instant.parse(updated_at),
    publishedAt = published_at?.let { Instant.parse(it) }
)

private fun NetworkPostAuthor.asExternalModel() = PostAuthor(
    id = id,
    displayName = display_name,
    avatarUrl = avatar_url ?: ""
)

private fun NetworkPostParent.asExternalModel() = PostParent(
    id = id,
    author = author.asExternalModel()
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
