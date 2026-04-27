package com.loom.core.network.serialization.polymorphic

import com.loom.core.network.model.NetworkCarouselElementObject
import com.loom.core.network.model.NetworkCarouselElementObjectEvent
import com.loom.core.network.model.NetworkCarouselElementObjectUser
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object NetworkCarouselElementObjectSerializer :
    JsonContentPolymorphicSerializer<NetworkCarouselElementObject>(NetworkCarouselElementObject::class) {

    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<out NetworkCarouselElementObject> {

        val type = element
            .jsonObject["objectType"]
            ?.jsonPrimitive
            ?.content

        return when (type) {
            "user_card" -> NetworkCarouselElementObjectUser.serializer()
            "event_card" -> NetworkCarouselElementObjectEvent.serializer()
            else -> error("Unknown type: $type")
        }
    }
}