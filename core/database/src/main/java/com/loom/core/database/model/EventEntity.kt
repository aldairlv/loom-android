package com.loom.core.database.model

/*
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creatorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("creatorId")]
)
data class EventEntity(
    @PrimaryKey val id: Long,
    val uuid: String,
    val title: String,
    val description: String,
    val date: String,
    val creatorId: String,
    val eventViewUrl: String,
    val poster: String,
    val joined: Boolean,
    val canBeJoined: Boolean,
    val isAdultOnly: Boolean,
    val numberParticipants: Int,
    val tags: List<String>
)

@Entity(
    tableName = "event_participants",
    primaryKeys = ["event_id", "user_id"],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventParticipantEntity(
    val event_id: Long,
    val user_id: String
)
*/
