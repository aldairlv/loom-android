package com.loom.core.model.data

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val userData: UserData) : SessionState
}