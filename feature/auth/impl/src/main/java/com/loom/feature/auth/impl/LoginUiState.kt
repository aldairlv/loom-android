package com.loom.feature.auth.impl


sealed interface LoginUiState{
    data object Idle: LoginUiState
    data object Loading: LoginUiState
    data object Success: LoginUiState
    data object Error: LoginUiState
}
