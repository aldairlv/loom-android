package com.loom.core.ui.feed

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.loom.core.model.data.FeedObject
import com.loom.core.ui.post.PostFeedCard

fun LazyListScope.feedObjects(
    objects: List<FeedObject>,
    onClickLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onRepost: (String) -> Unit,
    onShare: (String) -> Unit,
) {
    items(
        items = objects,
        key = { it.id }
    ) { feedObject ->
        when (feedObject) {
            is FeedObject.PostFeedObject -> {
                PostFeedCard(
                    postFeed = feedObject.post,
                    onClickLike = { onClickLike(feedObject.post.id) },
                    onComment = { onComment(feedObject.post.id) },
                    onRepost = { onRepost(feedObject.post.id) },
                    onShare = { onShare(feedObject.post.id) }
                )
            }
            // Future objects like Carousel or Title can be added here
        }
    }
}
