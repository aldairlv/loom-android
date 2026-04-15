package com.loom.core.data.repository

import com.loom.core.data.Syncable
import com.loom.core.model.data.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository : Syncable {
    fun getPosts(): Flow<List<Post>>
}