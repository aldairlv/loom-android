package com.loom.feature.foryou.impl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material3.Text

@Composable
fun ForYouScreen2(
    modifier: Modifier = Modifier,
    // Si en el futuro necesitas un ViewModel específico, lo inyectarías aquí
    // viewModel: ForYou2ViewModel = hiltViewModel(),
) {
    // Aquí podrías recolectar estados del ViewModel si fuera necesario

    ForYouScreen2int(
        modifier = modifier
    )
}

@Composable
internal fun ForYouScreen2int(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "funciona")
        }
    }
}