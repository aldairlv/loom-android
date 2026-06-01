package com.loom.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loom.core.model.data.PostAuthor
import com.loom.core.model.data.PostFeedContent
import com.loom.core.model.data.PostFeedItem
import com.loom.core.model.data.PostParent
import com.loom.core.model.data.LayoutRoot
import com.loom.core.model.data.PostInteractions
import com.loom.core.model.data.PostStats
import kotlinx.datetime.Instant

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: String,
    val author: PostAuthor,
    val parent: PostParent? = null,
    val root: PostParent? = null,
    val trail: List<PostFeedItem> = emptyList(),
    val status: String,
    val tags: List<String>,
    val contents: List<PostFeedContent>,
    val layout: List<LayoutRoot>,
    val interactions: PostInteractions? = null,
    val stats: PostStats? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant,
    @ColumnInfo(name = "published_at")
    val publishedAt: Instant? = null
)

// DB -> Model
fun PostEntity.asExternalPostModel() = PostFeedItem(
    id = id,
    author = author,
    parent = parent,
    root = root,
    trail = trail,
    status = status,
    tags = tags,
    contents = contents,
    layout = layout,
    interactions = interactions,
    stats = stats,
    createdAt = createdAt,
    updatedAt = updatedAt,
    publishedAt = publishedAt
)

fun PostFeedItem.asEntity() = PostEntity(
    id = id,
    author = author,
    parent = parent,
    root = root,
    trail = trail,
    status = status,
    tags = tags,
    contents = contents,
    layout = layout,
    interactions = interactions,
    stats = stats,
    createdAt = createdAt,
    updatedAt = updatedAt,
    publishedAt = publishedAt
)
