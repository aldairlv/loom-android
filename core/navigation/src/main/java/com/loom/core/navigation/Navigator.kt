package com.loom.core.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(
    val state: NavigationState,
    private val isLoggedIn: () -> Boolean,
    private val landingKey: NavKey
) {
    fun navigate(key: NavKey) {
        if (key is RequiresLogin && !isLoggedIn()) {
            navigate(landingKey)
            return
        }
        when (key) {
            state.currentTopLevelKey -> clearSubStack()
            in state.topLevelKeys -> goToTopLevel(key)
            else -> goToKey(key)
        }
    }

    fun goBack() {
        when (state.currentKey) {
            state.startKey -> { /* Manejar salida o error */ }
            state.currentTopLevelKey -> state.topLevelStack.removeLastOrNull()
            else -> state.currentSubStack.removeLastOrNull()
        }
    }

    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key)
            add(key)
        }
    }

    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startKey) clear() else remove(key)
            add(key)
        }
    }

    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }


    fun resetTo(key: NavKey) {
        state.topLevelStack.apply {
            clear()
            add(key)
        }
    }

    // O la versión combinada que sugeriste
    fun loginAndGoTo(key: NavKey) {
        state.currentSubStack.clear() // Limpia el flujo de auth actual
        resetTo(key) // Resetea el stack principal al destino final
    }
}