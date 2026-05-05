package com.loom.core.network.serialization.polymorphic

import com.loom.core.network.model.NetworkObjectTrendElement
import com.loom.core.network.model.NetworkObjectTrendElementTag
import com.loom.core.network.model.NetworkObjectTrendElementVideo
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object NetworkTrendElementObjectSerializer:
    JsonContentPolymorphicSerializer<NetworkObjectTrendElement>(
        NetworkObjectTrendElement::class
    ) {

    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<out NetworkObjectTrendElement> {
        val type = element
            .jsonObject["objectType"]
            ?.jsonPrimitive
            ?.content

        return when (type) {
            "tag" -> NetworkObjectTrendElementTag.serializer()
            "video"-> NetworkObjectTrendElementVideo.serializer()
            else -> error("Unknown type: $type")
        }
    }
}