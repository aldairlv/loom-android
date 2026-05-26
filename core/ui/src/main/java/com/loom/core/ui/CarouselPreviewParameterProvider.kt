package com.loom.core.ui
/*
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.loom.core.model.data.Carousel
import com.loom.core.model.data.CarouselUserElement
import com.loom.core.model.data.CarouselEventElement
import com.loom.core.model.data.EventProfile
import com.loom.core.model.data.Post
import com.loom.core.model.data.PostContent
import com.loom.core.model.data.UserProfile
//import com.loom.core.ui.PreviewData
import kotlin.time.Duration.Companion.minutes

object CarouselUsersPreviewParameterData {
    val carousel = Carousel(
        id = "kvnfvhjf2",
        type = "users",
        elements = listOf(
            CarouselUserElement(
                id = "1012",
                objectType = "user",
                resource = listOf(
                    UserProfile(
                        id = "1012",
                        username = "user",
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
                                id = "iu2hf3h",
                                blogId = 1013,
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
                                id = "iwhnier2y",
                                blogId = 10123,
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
                id = "10321",
                objectType = "user",
                resource = listOf(
                    UserProfile(
                        id = "103421",
                        username = "user",
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
                                id = "sdcee424f",
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
                                id = "sdvcdvsv",
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
                        username = "jeff",
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
                                id = "kvnfvhjf",
                                blogId = 101,
                                username = "jeff",
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
                                id = "kvnfvhjf",
                                blogId = 101,
                                username = "jeff",
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
                        username = "jeffer",
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
                                id = "kvnfvhjf",
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
                                id = "kvnfvhjf",
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
}

object CarouselEventsPreviewParameterData {
    val carousel = Carousel(
        id = "kvnfvhjf",
        type = "events",
        elements = listOf(
            CarouselEventElement(
                id = "202",
                objectType = "event",
                resource = listOf(
                    EventProfile(
                        id = 202,
                        creator =  UserProfile(
                            id = "101",
                            username = "jeffer",
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
                                    id = "kvnfvhjf",
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
                                    id = "kvnfvhjf",
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
                        ),
                        poster = "https://picsum.photos/id/2/800/400",
                        eventViewUrl = "https://example.com/event/202",
                        canBeJoined = true,
                        description = "Descripción del evento",
                        joined = false,
                        isAdultOnly = false,
                        title = "Título del evento",
                        uuid = "uuid-202",
                        tags = listOf("tag1", "tag2"),
                        numberParticipants = 100,
                        participants = listOf(
                            UserProfile(
                                id = "101",
                                username = "Ra",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                            ),
                            UserProfile(
                                id = "101",
                                username = "Dr.Isacs",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                            ),
                            UserProfile(
                                id = "101",
                                username = "PW.Anderson",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                        ),
                        date = "10/10/2023"
                    )
                )
            ),
            CarouselEventElement(
                id = "202",
                objectType = "event",
                resource = listOf(
                    EventProfile(
                        id = 202,
                        creator =  UserProfile(
                            id = "101",
                            username = "Feid",
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
                                    id = "kvnfvhjf",
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
                                    id = "kvnfvhjf",
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
                        ),
                        poster = "https://picsum.photos/id/2/800/400",
                        eventViewUrl = "https://example.com/event/202",
                        canBeJoined = true,
                        description = "Descripción del evento",
                        joined = false,
                        isAdultOnly = false,
                        title = "Título del evento",
                        uuid = "uuid-202",
                        tags = listOf("tag1", "tag2"),
                        numberParticipants = 100,
                        participants = listOf(
                            UserProfile(
                                id = "101",
                                username = "adeliaclark",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                            ),
                            UserProfile(
                                id = "101",
                                username = "JamesC",
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
                                        id = "kvnfvhjf",
                                        blogId = 101, username = "Jeffer",
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
                                        id = "kvnfvhjf",
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
                            ),
                            UserProfile(
                                id = "101",
                                username = "EverReady",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                        ),
                        date = "10/10/2023"
                    )
                )
            ),
            CarouselEventElement(
                id = "202",
                objectType = "event",
                resource = listOf(
                    EventProfile(
                        id = 202,
                        creator =  UserProfile(
                            id = "101",
                            username = "Ra",
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
                                    id = "kvnfvhjf",
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
                                    id = "kvnfvhjf",
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
                        ),
                        poster = "https://picsum.photos/id/2/800/400",
                        eventViewUrl = "https://example.com/event/202",
                        canBeJoined = true,
                        description = "Descripción del evento",
                        joined = false,
                        isAdultOnly = false,
                        title = "Título del evento",
                        uuid = "uuid-202",
                        tags = listOf("tag1", "tag2"),
                        numberParticipants = 100,
                        participants = listOf(
                            UserProfile(
                                id = "101",
                                username = "Ra",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                            ),
                            UserProfile(
                                id = "101",
                                username = "Ra",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                            ),
                            UserProfile(
                                id = "101",
                                username = "Ra",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                        ),
                        date = "10/10/2023"
                    )
                )
            ),
            CarouselEventElement(
                id = "202",
                objectType = "event",
                resource = listOf(
                    EventProfile(
                        id = 202,
                        creator =  UserProfile(
                            id = "101",
                            username = "Ra",
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
                                    id = "kvnfvhjf",
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
                                    id = "kvnfvhjf",
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
                        ),
                        poster = "https://picsum.photos/id/2/800/400",
                        eventViewUrl = "https://example.com/event/202",
                        canBeJoined = true,
                        description = "Descripción del evento",
                        joined = false,
                        isAdultOnly = false,
                        title = "Título del evento",
                        uuid = "uuid-202",
                        tags = listOf("tag1", "tag2"),
                        numberParticipants = 100,
                        participants = listOf(
                            UserProfile(
                                id = "101",
                                username = "Ra",
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                            ),
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                            ),
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
                                        id = "kvnfvhjf",
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
                                        id = "kvnfvhjf",
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
                        ),
                        date = "10/10/2023"
                    )
                )
            )
        )
    )
}

class CarouselUsersPreviewProvider : PreviewParameterProvider<Carousel> {
    override val values = sequenceOf(CarouselUsersPreviewParameterData.carousel)
}

class CarouselEventsPreviewProvider : PreviewParameterProvider<Carousel> {
    override val values = sequenceOf(CarouselEventsPreviewParameterData.carousel)
}
*/