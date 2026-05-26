package com.loom.core.database.model

/*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.loom.core.model.data.TrendCategory
import com.loom.core.model.data.UserProfile

@Entity(tableName = "trends")
data class TrendEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "object_type")
    val objectType: String,
    val category: String,
    val count: String,
    val iconUrl: String,
    val subType: String,
)

@Entity(
    tableName = "trend_items",
    primaryKeys = ["id", "trend_id"]
)
data class TrendItemEntity(
    @ColumnInfo(name = "id") // id del item
    val id: String,

    @ColumnInfo(name = "trend_id") // id del trend asociado
    val trendId: String,

    @ColumnInfo(name = "post_id") // Apunta al id del post
    val postId: String,

    @ColumnInfo(name = "tag_id")//apunta al id del tag asociado
    val tagId: String?,

    @ColumnInfo(name = "object_type") // category del trend -> tag
    val objectType: String,

    val position: Int  // Orden que manda el backend durante la sesión
)
*/
