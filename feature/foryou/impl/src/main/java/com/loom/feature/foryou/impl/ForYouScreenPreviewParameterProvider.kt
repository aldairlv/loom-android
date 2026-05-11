package com.loom.feature.foryou.impl

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.loom.core.model.data.Carousel
import com.loom.core.model.data.CarouselUserElement
import com.loom.core.model.data.Post
import com.loom.core.model.data.PostContent
import com.loom.core.model.data.TimelineObject
import com.loom.core.model.data.Title
import com.loom.core.model.data.UserProfile
import com.loom.core.ui.PreviewData
import com.loom.feature.foryou.impl.TimelinePreviewParameterData.timelineObjects
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

class TimelinePreviewParameterProvider : PreviewParameterProvider<List<TimelineObject>> {
    override val values = sequenceOf(timelineObjects)
}

object TimelinePreviewParameterData {
    private val now = Clock.System.now()
    val timelineObjects = listOf(
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiyq",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiyw",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiye",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiyr",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiyt",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.TitleObject(
            content = Title(
                id = "skdjcnsiyy",
                text = "Hechale un vistazo a estos Usuarios! 🚀"
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiya",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiys",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiyd",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiyf",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.PostObject(
            content = Post(
                id = "skdjcnsiyg",
                blogId = 101,
                username = "Conrad",
                timestamp = PreviewData.now.toEpochMilliseconds(),
                tags = listOf("android", "compose", "kotlin"),
                content = listOf(
                    PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                    //PostContent.Image("https://http.cat/images/100.jpg", 1080, 720),
                    //PostContent.Video("https://http.cat/images/100.jpg", 1080, 720)
                ),
                likesCount = 124,
                repostsCount = 12,
                commentsCount = 5,
                notesCount = 141,
                createdAt = PreviewData.now.minus(10.minutes),
                updatedAt = PreviewData.now.minus(10.minutes)
            )
        ),
        TimelineObject.TitleObject(
            content = Title(
                id = "skdjcnsiyok",
                text = "Hechale un vistazo a estos Usuarios! 🚀"
            )
        ),
        TimelineObject.CarouselObject(
            content = Carousel(
                id = "skdjcnsiyj",
                type = "users",
                elements = listOf(
                    CarouselUserElement(
                        id = "101",
                        objectType = "user",
                        resource = listOf(
                            UserProfile(
                                id = "101",
                                username = "MelonMusk",
                                avatar = "https://picsum.photos/id/1/800/400",
                                userViewUrl = "https://example.com/user/101",
                                canBeFollowed = true,
                                canShowBages = true,
                                description = "Descripción del usuario",
                                followed = false,
                                isAdult = false,
                                title = "Título del usuario",
                                uuid = "uuid-101",
                                posts = listOf(
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    ),
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    )
                                )
                            )
                        )
                    ),
                    CarouselUserElement(
                        id = "101",
                        objectType = "user",
                        resource = listOf(
                            UserProfile(
                                id = "101",
                                username = "User1",
                                avatar = "https://picsum.photos/id/1/800/400",
                                userViewUrl = "https://example.com/user/101",
                                canBeFollowed = true,
                                canShowBages = true,
                                description = "Descripción del usuario",
                                followed = false,
                                isAdult = false,
                                title = "Título del usuario",
                                uuid = "uuid-101",
                                posts = listOf(
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    ),
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    )
                                )
                            )
                        )
                    ),
                    CarouselUserElement(
                        id = "101",
                        objectType = "user",
                        resource = listOf(
                            UserProfile(
                                id = "101",
                                username = "User1",
                                avatar = "https://picsum.photos/id/1/800/400",
                                userViewUrl = "https://example.com/user/101",
                                canBeFollowed = true,
                                canShowBages = true,
                                description = "Descripción del usuario",
                                followed = false,
                                isAdult = false,
                                title = "Título del usuario",
                                uuid = "uuid-101",
                                posts = listOf(
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    ),
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    )
                                )
                            )
                        )
                    ),
                    CarouselUserElement(
                        id = "101",
                        objectType = "user",
                        resource = listOf(
                            UserProfile(
                                id = "101",
                                username = "User1",
                                avatar = "https://picsum.photos/id/1/800/400",
                                userViewUrl = "https://example.com/user/101",
                                canBeFollowed = true,
                                canShowBages = true,
                                description = "Descripción del usuario",
                                followed = false,
                                isAdult = false,
                                title = "Título del usuario",
                                uuid = "uuid-101",
                                posts = listOf(
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    ),
                                    Post(
                                        id = "skdjcnsiy",
                                        blogId = 101,
                                        username = "Jeffer",
                                        timestamp = PreviewData.now.toEpochMilliseconds(),
                                        tags = listOf("android", "compose", "kotlin"),
                                        content = listOf(
                                            PostContent.Text("¡Acabo de publicar mi primer proyecto en Compose! 🚀"),
                                            PostContent.Image("https://http.cat/images/100.jpg", 1080, 720)
                                        ),
                                        likesCount = 124,
                                        repostsCount = 12,
                                        commentsCount = 5,
                                        notesCount = 141,
                                        createdAt = PreviewData.now.minus(10.minutes),
                                        updatedAt = PreviewData.now.minus(10.minutes)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}