package com.loom.feature.explore.impl

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ExploreScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Aquí iría tu barra superior (TopAppBar)
        },
        bottomBar = {
            // Aquí iría tu barra de navegación (NavigationBar)
        },
        floatingActionButton = {
            // Aquí iría tu botón flotante (FAB)
        }
    ) { innerPadding ->
        // El contenido principal de la pantalla va aquí
        // Se usa 'innerPadding' para evitar que el contenido quede oculto tras las barras
        ExploreContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun ExploreContent(modifier: Modifier = Modifier) {
    // Aquí es donde empiezas a maquetar el cuerpo de la pantalla
}