package com.loom.core.data.repository

import com.loom.core.model.data.Comment
import com.loom.core.model.data.CommentFeed
import com.loom.core.model.data.FeedObject
import com.loom.core.model.data.PostMedia
import com.loom.core.model.data.PostsFeedResult
import com.loom.core.model.data.RowModel
import kotlinx.coroutines.flow.Flow

data class FeedObjectsResult(
    val objects: List<FeedObject>,
    val nextCursor: String?
)

interface HomeRepository {
    fun getFeedObjectsForYouFlow(): Flow<List<FeedObject>>
    suspend fun getPostsFeedForYou(cursor: String? = null): PostsFeedResult
    suspend fun getFeedObjectsForYou(cursor: String? = null, isRefresh: Boolean = false): FeedObjectsResult
    suspend fun getPostsFeedFollowing(cursor: String? = null): PostsFeedResult
    suspend fun getPostsFeedTags(cursor: String? = null): PostsFeedResult

    suspend fun uploadMedia(
        fileName: String,
        mimeType: String,
        fileBytes: ByteArray
    ): PostMedia

    suspend fun createPost(
        status: String,
        tags: List<String>,
        rows: List<RowModel>,
        parentId: String? = null,
        rootId: String? = null
    )

    suspend fun quickRepost(
        postId: String,
        parentId: String?,
        rootId: String?
    )

    suspend fun toggleLike(postId: String, isLiked: Boolean)

    suspend fun toggleFollow(profileId: String, isFollowed: Boolean)

    suspend fun getComments(postId: String, cursor: String? = null): CommentFeed
    suspend fun createComment(postId: String, text: String): Comment
    suspend fun createReply(postId: String, commentId: String, text: String): Comment
    suspend fun deleteComment(postId: String, commentId: String)
}
