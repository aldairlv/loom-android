package com.loom.core.database.model

/*
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

import com.loom.core.model.data.EventProfile // Tu modelo de dominio

data class PopulatedEventProfile(
    @Embedded val event: EventEntity,

    @Relation(
        parentColumn = "creatorId",
        entityColumn = "id"
    )
    val creator: UserEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = EventParticipantEntity::class,
            parentColumn = "event_id",
            entityColumn = "user_id"
        )
    )
    val participants: List<UserEntity>
)

/**
 * Mapper para convertir PopulatedEvent (BD) -> EventProfile (Modelo Externo)
 */
fun PopulatedEventProfile.asExternalEventProfileModel() = EventProfile(
    id = event.id,
    title = event.title,
    description = event.description,
    date = event.date,
    creator = creator.asExternalModel(),
    participants = participants.map { it.asExternalModel() },
    canBeJoined = event.canBeJoined,
    eventViewUrl = event.eventViewUrl,
    isAdultOnly = event.isAdultOnly,
    joined = event.joined,
    numberParticipants = event.numberParticipants,
    poster = event.poster,
    tags = event.tags,
    uuid = event.uuid
)
*/
