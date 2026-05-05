package com.loom.feature.explore.impl

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.loom.core.model.data.Post
import com.loom.core.model.data.PostContent
import com.loom.core.model.data.TagProfile
import com.loom.core.model.data.TimelineObject
import com.loom.core.model.data.Title
import com.loom.core.model.data.TrendCategory
import com.loom.core.model.data.TrendCategoryItem
import com.loom.core.model.data.TrendCategoryItemTag
import com.loom.core.model.enum.TrendType
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes
import com.loom.feature.explore.impl.ExplorePreviewParameterData.exploreObjects

class ExploreScreenPreviewParameterProvider : PreviewParameterProvider<List<TimelineObject>> {
    override val values = sequenceOf(exploreObjects)
}

object ExplorePreviewParameterData {
    private val now = Clock.System.now()
    val exploreObjects = listOf(
        TimelineObject.TitleObject(
            content = Title(
                id = "skdjcnsiy",
                text = "Temas del momento"
            )
        ),
        TimelineObject.TrendObject(
            content = TrendCategory(
                id = "1Lrrfc",
                category = "Music",
                iconUrl = "https://http.cat/images/100.jpg",
                count = "124 M de post",
                subType = TrendType.TAG,
                items = listOf(
                    TrendCategoryItemTag(
                        id = "1Lrrfc",
                        objectType = "tag",
                        tag = TagProfile(
                            name = "#Android",
                            isFollowed = false
                        ),
                        resource = listOf(
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
                            )
                        )
                    )
                )
            )
        )
    )
}