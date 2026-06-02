package com.loom.core.ui.post

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.loom.core.model.data.PostFeedItem


fun LazyListScope.feedPosts(
    posts: List<PostFeedItem>,
    onClickLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onQuickRepost: (String) -> Unit,
    onCommentRepost: (PostFeedItem) -> Unit,
    onShare: (String) -> Unit,
    onFollowClick: (String, String, Boolean) -> Unit = { _, _, _ -> },
) {
    items(
        items = posts,
        key = { it.id }
    ) { post ->
        PostFeedCard(
            postFeed = post,
            onClickLike = { onClickLike(post.id) },
            onComment = { onComment(post.id) },
            onQuickRepost = { onQuickRepost(post.id) },
            onCommentRepost = onCommentRepost,
            onShare = { onShare(post.id) },
            onFollowClick = { onFollowClick(post.id, post.author.id, post.author.isFollowed) }
        )
    }
}
