package com.loom.core.model.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface FeedObject {
    val id: String
    val streamGlobalPosition: Int
    val streamSessionId: String?

    @Serializable
    data class PostFeedObject(
        val post: PostFeedItem,
        override val streamGlobalPosition: Int,
        override val streamSessionId: String?
    ) : FeedObject {
        override val id: String = post.id
    }

    @Serializable
    data class EventFeedObject(
        val event: EventFeedItem,
        override val streamGlobalPosition: Int,
        override val streamSessionId: String?
    ) : FeedObject {
        override val id: String = event.id
    }

    // Future objects can be added here
}
