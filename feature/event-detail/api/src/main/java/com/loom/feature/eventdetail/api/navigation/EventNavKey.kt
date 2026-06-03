package com.loom.feature.eventdetail.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class EventNavKey(val eventId: String) : NavKey
