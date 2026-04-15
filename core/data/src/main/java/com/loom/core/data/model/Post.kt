package com.loom.core.data.model

import com.loom.core.database.model.PostEntity
import com.loom.core.network.model.NetworkPost

// Mappers: De Red a Base de Datos
fun NetworkPost.asEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    imageUrl = imageUrl,
    publishDate = publishDate
)

// Nota: El paso de Entity a Dominio (asExternalModel)
// ya está en el módulo database.