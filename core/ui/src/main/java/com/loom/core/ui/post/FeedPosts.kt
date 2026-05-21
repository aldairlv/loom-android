package com.loom.core.ui.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.loom.core.model.data.PostFeedItem


fun LazyListScope.feedPosts(
    posts: List<PostFeedItem>,
    onClickLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onRepost: (String) -> Unit,
    onShare: (String) -> Unit,
) {
    items(
        items = posts,
        key = { it.id }
    ) { post ->
        PostFeedCard(
            postFeed = post,
            onClickLike = { onClickLike(post.id) },
            onComment = { onComment(post.id) },
            onRepost = { onRepost(post.id) },
            onShare = { onShare(post.id) }
        )
    }
}
