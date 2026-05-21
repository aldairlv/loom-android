package com.loom.feature.home.impl

import androidx.lifecycle.ViewModel
import com.loom.core.data.repository.HomeRepository
import com.loom.core.ui.tabs.TabItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    //private val postRepository: PostRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {
    private val _tabs = MutableStateFlow(
        listOf(
            HomeTab.ForYou.toTabItem(enabled = true),
            HomeTab.Following.toTabItem(enabled = true),
            HomeTab.Tags.toTabItem(enabled = true)
        )
    )
    val tabs: StateFlow<List<TabItem>> = _tabs.asStateFlow()

    fun toggleTab(key: String, enabled: Boolean) {
        _tabs.update { currentTabs ->
            currentTabs.map { tab ->
                if (tab.key == key) {
                    tab.copy(enabled = enabled)
                } else {
                    tab
                }
            }
        }

        // Aquí podrías llamar a un UseCase o Repositorio
        // para guardar este cambio de forma permanente.
    }
}