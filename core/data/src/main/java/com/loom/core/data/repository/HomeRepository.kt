package com.loom.core.data.repository

import com.loom.core.model.data.PostMedia
import com.loom.core.model.data.PostsFeedResult
import com.loom.core.model.data.RowModel

interface HomeRepository {
    suspend fun getPostsFeedForYou(cursor: String? = null): PostsFeedResult
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
        rows: List<RowModel>
    )
}
