package com.loom.core.ui.tabs

import androidx.compose.runtime.Immutable


@Immutable
data class TabItem(
    val key: String,
    val title: String,
    val enabled: Boolean = true,
)