package com.loom.core.network.model

import com.loom.core.network.serialization.polymorphic.NetworkObjectSerializer
import kotlinx.serialization.Serializable

@Serializable(with = NetworkObjectSerializer::class)
sealed interface NetworkObject{
    val objectType: String
    val id: String
    val streamGlobalPosition: Int
    val streamSessionId: String?
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