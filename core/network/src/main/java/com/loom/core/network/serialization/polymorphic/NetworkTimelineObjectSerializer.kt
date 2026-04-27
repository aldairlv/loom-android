package com.loom.core.network.serialization.polymorphic

import com.loom.core.network.model.NetworkTimelineObject
import com.loom.core.network.model.NetworkTimelineObjectCarousel
import com.loom.core.network.model.NetworkTimelineObjectPost
import com.loom.core.network.model.NetworkTimelineObjectTitle
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object NetworkTimelineObjectSerializer :
    JsonContentPolymorphicSerializer<NetworkTimelineObject>(NetworkTimelineObject::class) {

    override fun selectDeserializer(
        element: JsonElement
    ): DeserializationStrategy<out NetworkTimelineObject> {

        val type = element
            .jsonObject["objectType"]
            ?.jsonPrimitive
            ?.content

        return when (type) {
            "post" -> NetworkTimelineObjectPost.serializer()
            "title" -> NetworkTimelineObjectTitle.serializer()
            "carousel" -> NetworkTimelineObjectCarousel.serializer()
            else -> error("Unknown type: $type")
        }
    }
}