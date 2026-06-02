package com.loom.core.ui.feed

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.loom.core.model.data.FeedObject
import com.loom.core.model.data.PostFeedItem
import com.loom.core.ui.event.EventFeedCard
import com.loom.core.ui.post.PostFeedCard

fun LazyListScope.feedObjects(
    objects: List<FeedObject>,
    onClickLike: (String) -> Unit,
    onComment: (String) -> Unit,
    onQuickRepost: (String) -> Unit,
    onCommentRepost: (PostFeedItem) -> Unit,
    onShare: (String) -> Unit,
    onFollowClick: (String, String, Boolean) -> Unit, // postId, authorId, isFollowed
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
                    onQuickRepost = { onQuickRepost(feedObject.post.id) },
                    onCommentRepost = onCommentRepost,
                    onShare = { onShare(feedObject.post.id) },
                    onFollowClick = { 
                        onFollowClick(
                            feedObject.post.id, 
                            feedObject.post.author.id, 
                            feedObject.post.author.isFollowed
                        ) 
                    }
                )
            }
            is FeedObject.EventFeedObject -> {
                EventFeedCard(
                    eventFeed = feedObject.event,
                    onClickLike = { onClickLike(feedObject.event.id) },
                    onComment = { onComment(feedObject.event.id) },
                    onShare = { onShare(feedObject.event.id) },
                    onFollowClick = {
                        onFollowClick(
                            feedObject.event.id,
                            "", // authorId not directly in EventCreator for now
                            false
                        )
                    }
                )
            }
        }
    }
}
