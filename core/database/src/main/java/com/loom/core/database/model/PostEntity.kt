package com.loom.core.database.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loom.core.model.data.Post
import com.loom.core.model.data.PostContent
import kotlinx.datetime.Instant
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "blog_id")
    val blogId: Int,
    val timestamp: Long,
    val tags: List<String>,
    @ColumnInfo(name = "content_blocks")
    val contentBlocks: List<PostContent>, // Necesitarás un TypeConverter para esto
    @ColumnInfo(name = "likes_count")
    val likesCount: Int,
    @ColumnInfo(name = "reposts_count")
    val repostsCount: Int,
    @ColumnInfo(name = "comments_count")
    val commentsCount: Int,
    @ColumnInfo(name = "notes_count")
    val notesCount: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant
)

fun PostEntity.asExternalModel() = Post(
    id = id,
    blogId = blogId,
    timestamp = timestamp,
    tags = tags,
    contentBlocks = contentBlocks,
    likesCount = likesCount,
    reposts_count = repostsCount,
    commentsCount = commentsCount,
    notesCount = notesCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)