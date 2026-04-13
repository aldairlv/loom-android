package com.loom.core.database.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loom.core.model.data.Post
import kotlinx.datetime.Instant

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: String,
    val author: String,
    val content: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "publish_date")
    val publishDate: Instant,
)

// Esta es la función clave que viste en NiA para convertir de DB a UI
fun PostEntity.asExternalModel() = Post(
    id = id,
    author = author,
    content = content,
    imageUrl = imageUrl,
    publishDate = publishDate
)