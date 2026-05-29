package com.loom.feature.settings.impl

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.theme.LoomTheme

@Composable
fun AccountSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AccountSettingsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onOptimizeVideosChanged = viewModel::onOptimizeVideosChanged,
        onDisableDoubleTapLikeChanged = viewModel::onDisableDoubleTapLikeChanged,
        onLogout = viewModel::logout,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountSettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onOptimizeVideosChanged: (Boolean) -> Unit,
    onDisableDoubleTapLikeChanged: (Boolean) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "¿Seguro que quieres cerrar la sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    onLogout()
                    showLogoutDialog = false
                }) {
                    Text(text = "Si")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Configuración") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                SettingsRow(
                    title = "Correo electrónico",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Contraseña",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Fecha de nacimiento y grupo de edad",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Seguridad",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Notificaciones",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Mensajes",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Respuestas",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Sonidos",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Privacidad",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Menciones",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Contenido visible",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Paleta de colores",
                    onClick = { /* TODO */ }
                )
            }

            item {
                SectionHeader(title = "Subir y descargar contenido")
            }

            item {
                SettingsRow(
                    title = "Reproduccion automática",
                    onClick = { /* TODO */ }
                )
            }

            item {
                SettingsSwitchRow(
                    title = "Optimizar videos",
                    subtitle = "Activa esta opción para redimensionar y comprimir los videos antes de subirlos.",
                    checked = uiState.optimizeVideos,
                    onCheckedChange = onOptimizeVideosChanged
                )
            }

            item {
                SettingsSwitchRow(
                    title = "Desactivar doble toque para <<Me gusta>>",
                    checked = uiState.disableDoubleTapLike,
                    onCheckedChange = onDisableDoubleTapLikeChanged
                )
            }

            item {
                SectionHeader(title = "Información legal")
            }

            item {
                SettingsRow(
                    title = "Condiciones",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Política de privacidad",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Panel de control de privacidad",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Accesibilidad",
                    onClick = { /* TODO */ }
                )
            }
            item {
                SettingsRow(
                    title = "Créditos",
                    onClick = { /* TODO */ }
                )
            }

            item {
                SectionHeader(title = "Cuenta")
            }

            item {
                SettingsRow(
                    title = "Ayuda",
                    leadingIcon = Icons.Default.Help,
                    onClick = { /* TODO */ }
                )
            }

            item {
                SettingsRow(
                    title = "Denunciar un abuso",
                    leadingIcon = Icons.Default.Flag,
                    onClick = { /* TODO */ }
                )
            }

            item {
                SettingsRow(
                    title = "Laboratorio de ideas",
                    leadingIcon = Icons.Default.Science,
                    onClick = { /* TODO */ }
                )
            }

            item {
                SettingsRow(
                    title = "Borrar cuenta",
                    leadingIcon = Icons.Default.Delete,
                    onClick = { /* TODO */ }
                )
            }

            item {
                SettingsRow(
                    title = "Cerrar sesion",
                    leadingIcon = Icons.Default.PowerSettingsNew,
                    trailing = {
                        Text(
                            text = uiState.profile?.username ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        if (trailing != null) {
            trailing()
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AccountSettingsScreenPreview() {
    LoomTheme {
        AccountSettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {},
            onOptimizeVideosChanged = {},
            onDisableDoubleTapLikeChanged = {},
            onLogout = {}
        )
    }
}
