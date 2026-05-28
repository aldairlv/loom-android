package com.loom.core.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.loom.core.model.data.UserAccountProfile

@Composable
fun ProfileHeaderContent(
    profile: UserAccountProfile?,
    isEditMode: Boolean,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
        // 1. Banner + TopBar + Avatar (Estructura superpuesta)
        Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            // RUTA BANNER
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer)) {
                profile?.bannerUrl?.let { bannerUrl ->
                    AsyncImage(
                        model = bannerUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                if (isEditMode) {
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Cambiar Banner",
                            tint = Color.White
                        )
                    }
                }
            }

            // TOP BAR (Botones superiores)
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp).padding(horizontal = 16.0.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = "Editar Perfil"
                    )
                }
            }

            // AVATAR (Posicionado abajo a la izquierda, sobresaliendo de la caja)
            Surface(
                modifier = Modifier.align(Alignment.BottomStart).offset(x = 16.dp, y = 36.dp).size(72.dp),
                shape = CircleShape,
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.surface),
                color = MaterialTheme.colorScheme.secondary
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    profile?.avatarUrl?.let { avatarUrl ->
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (isEditMode) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Cambiar Avatar",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Margen para compensar el desfase vertical del avatar (offset y = 36.dp)
        Spacer(modifier = Modifier.height(42.dp))

        // 2. FILA DE DISPLAY NAME O TITLE
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(text = profile?.displayName ?: "Cargando...", style = MaterialTheme.typography.titleLarge)
            Text(text = "@${profile?.username ?: ""}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }

        // 3. FILA PARA BADGES
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            // Ejemplo: Badge 1, Badge 2...
            if (profile?.canBeFollowed == true) {
                SuggestionChip(onClick = {}, label = { Text("Seguible") })
            }
        }

        // 4. FILA PARA DESCRIPCIÓN / BIO
        Text(
            text = profile?.bio ?: "",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        // 5. FILA ADICIONAL PARA BOTONES (Tipo preguntas, etc.)
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {}) { Text("Hacer Pregunta") }
            OutlinedButton(onClick = {}) { Text("Mensaje") }
        }
    }
}