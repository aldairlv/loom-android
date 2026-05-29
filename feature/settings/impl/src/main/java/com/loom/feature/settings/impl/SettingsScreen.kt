package com.loom.feature.settings.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import androidx.compose.foundation.background
import com.loom.core.designsystem.theme.LoomTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onAccountSettingsClick = onAccountSettingsClick,
        onAllowQuestionsChanged = viewModel::onAllowQuestionsChanged,
        onAllowAnonymousQuestionsChanged = viewModel::onAllowAnonymousQuestionsChanged,
        onAllowMultimediaQuestionsChanged = viewModel::onAllowMultimediaQuestionsChanged,
        onShowPopularPostsChanged = viewModel::onShowPopularPostsChanged,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    onAllowQuestionsChanged: (Boolean) -> Unit,
    onAllowAnonymousQuestionsChanged: (Boolean) -> Unit,
    onAllowMultimediaQuestionsChanged: (Boolean) -> Unit,
    onShowPopularPostsChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cuenta") },
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
                    title = "Configuración de la cuenta",
                    onClick = { onAccountSettingsClick() }
                )
            }

            item {
                SectionHeader(title = uiState.profile?.username ?: "Usuario")
            }

            item {
                SettingsRow(
                    title = "Cambiar el username",
                    onClick = { /* TODO: Redirect */ }
                )
            }

            item {
                SettingsRow(
                    title = "Páginas",
                    subtitle = "páginas visibles en tu perfil",
                    onClick = { /* TODO: Redirect */ }
                )
            }

            item {
                SettingsRow(
                    title = "Etiquetas destacadas",
                    onClick = { /* TODO: Redirect */ }
                )
            }

            item {
                SettingsRow(
                    title = "Seguidores",
                    trailing = {
                        Text(
                            text = "0", // TODO: Get from profile or somewhere else
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    onClick = { /* TODO: Redirect */ }
                )
            }

            item {
                SettingsRow(
                    title = "Borradores",
                    onClick = { /* TODO: Redirect */ }
                )
            }

            item {
                SettingsRow(
                    title = "Bandeja de entrada",
                    onClick = { /* TODO: Redirect */ }
                )
            }

            item {
                SectionHeader(title = "Visibilidad")
            }

            item {
                SettingsSwitchRow(
                    title = "Permitir que te hagan preguntas",
                    checked = uiState.allowQuestions,
                    onCheckedChange = onAllowQuestionsChanged
                )
            }

            item {
                SettingsSwitchRow(
                    title = "Permitir las preguntas anónimas",
                    checked = uiState.allowAnonymousQuestions,
                    onCheckedChange = onAllowAnonymousQuestionsChanged
                )
            }

            item {
                SettingsSwitchRow(
                    title = "Permitir las preguntas con contenido multimedia",
                    checked = uiState.allowMultimediaQuestions,
                    onCheckedChange = onAllowMultimediaQuestionsChanged
                )
            }

            item {
                SettingsSwitchRow(
                    title = "Mostrar publicaciones populares",
                    checked = uiState.showPopularPosts,
                    onCheckedChange = onShowPopularPostsChanged
                )
            }

            item {
                SettingsRow(
                    title = "Usuarios bloqueados",
                    onClick = { /* TODO: Redirect */ }
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
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenPreview() {
    LoomTheme {
        SettingsScreen(
            uiState = SettingsUiState(),
            onBackClick = {},
            onAccountSettingsClick = {},
            onAllowQuestionsChanged = {},
            onAllowAnonymousQuestionsChanged = {},
            onAllowMultimediaQuestionsChanged = {},
            onShowPopularPostsChanged = {}
        )
    }
}
