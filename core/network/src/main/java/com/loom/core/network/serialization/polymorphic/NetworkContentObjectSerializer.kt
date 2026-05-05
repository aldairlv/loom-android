package com.loom.core.network.serialization.polymorphic

import com.loom.core.network.model.NetworkContentObject
import com.loom.core.network.model.NetworkContentObjectImage
import com.loom.core.network.model.NetworkContentObjectText
import com.loom.core.network.model.NetworkContentObjectVideo
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object NetworkContentObjectSerializer :
    JsonContentPolymorphicSerializer<NetworkContentObject>(NetworkContentObject::class) {

    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<out NetworkContentObject> {

        val type = element
            .jsonObject["type"]
            ?.jsonPrimitive
            ?.content

        return when (type) {
            "image" -> NetworkContentObjectImage.serializer()
            "text" -> NetworkContentObjectText.serializer()
            "video" -> NetworkContentObjectVideo.serializer()
            else -> error("Unknown type: $type")
        }
    }
}

