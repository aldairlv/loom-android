package com.loom.core.model.data


import java.util.UUID

// ---------------------------------------------------------------------------
// Data Models
// ---------------------------------------------------------------------------

sealed interface Block {
    val id: String
}

data class TextBlock(
    override val id: String = UUID.randomUUID().toString(),
    val text: String = ""
) : Block

data class ImageBlock(
    override val id: String = UUID.randomUUID().toString(),
    val imgUrl: String,
    val mimeType: String,
    val width: Int,
    val height: Int,
    // Guardamos las dimensiones nativas para poder recalcular siempre sin pérdidas
    val originalWidth: Int = width,
    val originalHeight: Int = height,
    val isUploading: Boolean = false,
    val backendId: String? = null
) : Block

data class VideoBlock(
    override val id: String = UUID.randomUUID().toString(),
    val videoUrl: String,
    val mimeType: String,
    val width: Int,
    val height: Int,
    // Guardamos las dimensiones nativas para poder recalcular siempre sin pérdidas
    val originalWidth: Int = width,
    val originalHeight: Int = height,
    val isUploading: Boolean = false,
    val backendId: String? = null
) : Block

data class RowModel(
    val id: String,
    val blocks: List<Block> = emptyList()
)

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
}
