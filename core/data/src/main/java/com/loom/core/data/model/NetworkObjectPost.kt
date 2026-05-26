package com.loom.core.data.model
/*
import com.loom.core.database.model.PostEntity
import com.loom.core.network.model.NetworkObjectPost
import com.loom.core.model.data.PostContent
import com.loom.core.network.model.NetworkContentObject
import com.loom.core.network.model.NetworkContentObjectImage
import com.loom.core.network.model.NetworkContentObjectText
import com.loom.core.network.model.NetworkContentObjectVideo
import com.loom.core.network.model.NetworkPost

// Json -> Model -> DB
fun NetworkObjectPost.asInternalPostEntity(): PostEntity {
    return PostEntity(
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
        content = content.map { it.asPostContent() }
    )
}


// Support functions
fun NetworkContentObject.asPostContent(): PostContent {
    return when (this) {
        is NetworkContentObjectText -> PostContent.Text(
            text = this.text
        )
        is NetworkContentObjectImage -> PostContent.Image(
            imageUrl = this.media.firstOrNull()?.url ?: "", // Accedes al primero
            width = this.media.firstOrNull()?.width ?: 0,
            height = this.media.firstOrNull()?.height ?: 0
        )
        is NetworkContentObjectVideo -> PostContent.Video(
            videoUrl = this.media.firstOrNull()?.url ?: "", // Accedes al primero
            width = this.media.firstOrNull()?.width ?: 0,
            height = this.media.firstOrNull()?.height ?: 0
        )
    }
}


fun NetworkPost.asInternalUserPostEntity(): PostEntity {
    return PostEntity(
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
        content = content.map { it.asPostContent() }
    )
}

 */