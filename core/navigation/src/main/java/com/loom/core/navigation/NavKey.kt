package com.loom.core.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Interface to mark a [NavKey] that requires the user to be logged in.
 */
interface RequiresLogin

sealed interface AppNavKey : NavKey {
    val graph: Graph
}

enum class Graph {
    AUTH,
    MAIN
}
