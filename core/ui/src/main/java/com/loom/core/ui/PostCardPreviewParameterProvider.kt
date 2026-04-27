package com.loom.core.ui


import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.loom.core.model.data.Post // Asegúrate de importar tu modelo real
import com.loom.core.model.data.PostContent
import com.loom.core.ui.PreviewData.post
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class PostCardPreviewParameterProvider : PreviewParameterProvider<Post> {
    override val values: Sequence<Post> = sequenceOf(post)
}

object PreviewData {
    val now = Clock.System.now()
    val post = Post(
        id = "1Lrrfc",
        blogId = 101,
        username = "Jeffer",
        timestamp = now.toEpochMilliseconds(),
        tags = listOf("android", "compose", "kotlin"),
        content = listOf(
            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
        ),
        likesCount = 124,
        repostsCount = 12,
        commentsCount = 5,
        notesCount = 141,
        createdAt = now.minus(10.minutes),
        updatedAt = now.minus(10.minutes)
    )
}