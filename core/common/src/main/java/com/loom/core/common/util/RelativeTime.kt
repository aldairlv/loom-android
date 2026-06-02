package com.loom.core.common.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.absoluteValue

fun Instant.toRelativeTimeSpan(): String {
    val now = Clock.System.now()
    val duration = now - this
    val seconds = duration.inWholeSeconds.absoluteValue

    return when {
        seconds < 60 -> "ahora"
        seconds < 3600 -> "${seconds / 60}m"
        seconds < 86400 -> "${seconds / 3600}h"
        seconds < 604800 -> "${seconds / 86400}d"
        seconds < 2419200 -> "${seconds / 604800}sem"
        seconds < 31536000 -> "${seconds / 2592000}mes"
        else -> "${seconds / 31536000}a"
    }
}
