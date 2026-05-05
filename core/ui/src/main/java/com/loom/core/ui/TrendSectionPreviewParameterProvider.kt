package com.loom.core.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.loom.core.model.data.Post
import com.loom.core.model.data.PostContent
import com.loom.core.model.data.TagProfile
import com.loom.core.model.data.TrendCategory
import com.loom.core.model.data.TrendCategoryItem
import com.loom.core.model.data.TrendCategoryItemTag
import com.loom.core.model.enum.TrendType
import com.loom.core.ui.TrendCategoryPreviewData.trendCategory
//import com.loom.core.ui.TrendContentCardPreviewData.now
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes


class TrendSectionPreviewParameterProvider : PreviewParameterProvider<TrendCategory> {
    override val values: Sequence<TrendCategory> = sequenceOf(trendCategory)
}

object TrendCategoryPreviewData {
    private val now = Clock.System.now()
    val trendCategory = TrendCategory(
        id = "111",
        category = "Music",
        iconUrl = "https://http.cat/images/100.jpg",
        count = "124 M de post",
        subType = TrendType.TAG,
        items = listOf(
            TrendCategoryItemTag(
                id = "222",
                objectType = "tag",
                tag = TagProfile(
                    name = "android",
                    isFollowed = false,
                ),
                resource = listOf(
                    Post(
                        id = "333",
                        blogId = 333,
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
                )
            )
        )
    )
}
