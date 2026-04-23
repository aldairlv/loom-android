package com.loom.core.data.model

import com.loom.core.database.model.PostEntity
import com.loom.core.model.data.PostContent
import com.loom.core.network.model.NetworkContentBlock
import com.loom.core.network.model.NetworkPost

// Mappers: De Red a Base de Datos
fun NetworkPost.asEntity() = PostEntity(
    id = id,
    blogId = blog,
    timestamp = timestamp,
    tags = tags,
    contentBlocks = contentBlocks.map { it.asExternalModel() },
    likesCount = likesCount,
    repostsCount = repostsCount,
    commentsCount = commentsCount,
    notesCount = notesCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun NetworkContentBlock.asExternalModel(): PostContent {
    return when (type) {
        "text" -> PostContent.Text(text = text ?: "")
        "image" -> {
            val media = media?.firstOrNull()
            PostContent.Image(
                imageUrl = media?.url ?: "",
                width = media?.width ?: 0,
                height = media?.height ?: 0
            )
        }
        else -> PostContent.Text("") // Fallback
    }
}
// Nota: El paso de Entity a Dominio (asExternalModel)
// ya está en el módulo database.