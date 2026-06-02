package com.loom.core.network.serialization.polymorphic

import com.loom.core.network.model.NetworkObject
import com.loom.core.network.model.NetworkObjectCarousel
import com.loom.core.network.model.NetworkObjectEvent
import com.loom.core.network.model.NetworkObjectPost
import com.loom.core.network.model.NetworkObjectTitle
import com.loom.core.network.model.NetworkObjectTrend
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object NetworkObjectSerializer :
    JsonContentPolymorphicSerializer<NetworkObject>(NetworkObject::class) {

    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<out NetworkObject> {

        val type = element
            .jsonObject["objectType"]
            ?.jsonPrimitive
            ?.content

        return when (type) {
            "post" -> NetworkObjectPost.serializer()
            "title" -> NetworkObjectTitle.serializer()
            "carousel" -> NetworkObjectCarousel.serializer()
            "trend" -> NetworkObjectTrend.serializer()
            "event" -> NetworkObjectEvent.serializer()
            else -> error("Unknown type: $type")
        }
    }
}