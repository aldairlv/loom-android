package com.loom.core.data.model

import com.loom.core.database.model.EventEntity
import com.loom.core.database.model.EventParticipantEntity
import com.loom.core.network.model.NetworkEvent

fun NetworkEvent.asInternalEventEntity() = EventEntity(
    id = id,
    uuid = uuid,
    title = title,
    description = description,
    date = date,
    creatorId = creator.id,
    eventViewUrl = eventViewUrl,
    poster = poster,
    joined = joined,
    canBeJoined = canBeJoined,
    isAdultOnly = isAdultOnly,
    numberParticipants = numberParticipants,
    tags = tags
)

fun NetworkEvent.asParticipantEntities(): List<EventParticipantEntity> {
    return participants.map {
        EventParticipantEntity(
            event_id = id,
            user_id = it.id
        )
    }
}