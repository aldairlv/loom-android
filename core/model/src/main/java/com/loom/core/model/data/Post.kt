package com.loom.core.model.data
import kotlinx.datetime.Instant
data class Post(
    val id: String,
    val author: String,
    val content: String,
    val imageUrl: String?,
    val publishDate: Instant
)