package com.loom.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("user_card")
class NetworkCarouselElementObjectUser (
    override val id: String,
    val resource: List<NetworkUser>,
): NetworkCarouselElementObject {

}