package com.loom.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.loom.core.model.data.Post // Asegúrate de importar tu modelo real
import com.loom.core.model.data.PostContent
import com.loom.core.ui.PreviewParameterData.posts
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class PostFeedPreviewParameterProvider : PreviewParameterProvider<List<Post>> {
    override val values = sequenceOf(posts)
}

object PreviewParameterData {
    private val now = Clock.System.now()
    val posts = listOf(
        Post(
            id = "kvnfvhjf",
            blogId = 101,
            username = "Page One",
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
        ),
        Post(
            id = "2Lscd",
            blogId = 102,
            username = "userw3",
            timestamp = now.minus(2.hours).toEpochMilliseconds(),
            tags = listOf("ui", "ux"),
            content = listOf(
                PostContent.Text("Explorando las bondades de PreviewParameterProvider para organizar mis estados de UI.")
            ),
            likesCount = 45,
            repostsCount = 2,
            commentsCount = 0,
            notesCount = 47,
            createdAt = now.minus(2.hours),
            updatedAt = now.minus(1.hours)
        ),
        Post(
            id = "3Lcdcsc",
            blogId = 103,
            username = "Conrad",
            timestamp = now.minus(1.days).toEpochMilliseconds(),
            tags = listOf("tips", "arch"),
            content = listOf(
                PostContent.Text("Tip del día: Usa estados sellados (Sealed Classes) para gestionar la carga de datos."),
                PostContent.Image("https://example.com/diagram.png", 800, 600)
            ),
            likesCount = 890,
            repostsCount = 150,
            commentsCount = 23,
            notesCount = 1063,
            createdAt = now.minus(1.days),
            updatedAt = now.minus(1.days)
        ),
        Post(
            id = "3ihif",
            blogId = 104,
            username = "Jeffer",
            timestamp = Instant.parse("2026-04-10T12:00:00Z").toEpochMilliseconds(),
            tags = listOf("ios", "swift"),
            content = listOf(
                PostContent.Text("Tip del día 4: Diferencias en el manejo de estados entre plataformas.")
            ),
            likesCount = 12,
            repostsCount = 1,
            commentsCount = 1,
            notesCount = 14,
            createdAt = Instant.parse("2026-04-10T12:00:00Z"),
            updatedAt = Instant.parse("2026-04-10T12:00:00Z")
        )
    )
}