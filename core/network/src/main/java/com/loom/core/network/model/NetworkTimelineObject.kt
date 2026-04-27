package com.loom.core.network.model

import com.loom.core.network.serialization.polymorphic.NetworkTimelineObjectSerializer
import kotlinx.serialization.Serializable

@Serializable(with = NetworkTimelineObjectSerializer::class)
sealed interface NetworkTimelineObject{
    val objectType: String
    val id: String
    val streamGlobalPosition: Int
}


























// Cambiamos a String por los hashes del backend
//@SerialName("objectType")
//val objectType: String, // "post", "title", "carousel"

//@SerialName("streamGlobalPosition")


// Aquí mapeamos los contenidos posibles.
// Solo uno vendrá lleno dependiendo del objectType.
//val post: NetworkPost? = null,
//val title: NetworkTitle? = null, // Para el objeto tipo "title"
//val carousel: NetworkCarousel? = null // Para el objeto tipo "carousel"