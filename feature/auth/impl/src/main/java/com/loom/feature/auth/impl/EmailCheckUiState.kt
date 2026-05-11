package com.loom.feature.auth.impl

sealed interface EmailCheckUiState {
    data object Idle : EmailCheckUiState
    data object Loading : EmailCheckUiState
    data object Error : EmailCheckUiState
    data object ExistingUser : EmailCheckUiState
    data object NewUser : EmailCheckUiState
}