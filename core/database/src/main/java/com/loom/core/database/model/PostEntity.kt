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
    val id: String,
    @ColumnInfo(name = "blog_id")
    val blogId: Int,
    val username: String,
    val timestamp: Long,
    val tags: List<String>,
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
    val updatedAt: Instant,
    val content: List<PostContent> // Se guarda como String/JSON en la DB via TypeConverter
)

// DB -> Model
fun PostEntity.asExternalPostModel() = Post(
    id = id,
    blogId = blogId,
    username = username,
    timestamp = timestamp,
    likesCount = likesCount,
    repostsCount = repostsCount,
    commentsCount = commentsCount,
    notesCount = notesCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
    tags = tags,
    content = content
)
