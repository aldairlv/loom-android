package com.loom.core.ui.profile

import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.R.attr.textColor
import android.content.res.Configuration
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.model.data.UserAccountProfile

@Composable
fun ProfileHeaderContent(
    profile: UserAccountProfile?,
    isEditMode: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    draftDisplayName: String,
    onDisplayNameChange: (String) -> Unit,
    draftBio: String,
    onBioChange: (String) -> Unit,
    draftAvatarUri: Uri? = null,
    onAvatarClick: () -> Unit = {},
    draftBannerUri: Uri? = null,
    onBannerClick: () -> Unit = {},
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showExitDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("¿Guardar cambios?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onSaveClick()
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onCancelClick()
                }) {
                    Text("Descartar")
                }
            }
        )
    }

    Column(modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)) {
        // 1. Banner + TopBar + Avatar (Estructura superpuesta)
        Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            // RUTA BANNER
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer)) {
                val bannerModel = draftBannerUri ?: profile?.bannerUrl
                bannerModel?.let { model ->
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(model)
                            .allowHardware(false)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                if (isEditMode) {
                    IconButton(
                        onClick = onBannerClick,
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
                horizontalArrangement = if (isEditMode) Arrangement.SpaceBetween else Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEditMode) {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar"
                        )
                    }
                    TextButton(onClick = onSaveClick) {
                        Text("Guardar", style = MaterialTheme.typography.labelLarge)
                    }
                } else {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = LoomIcons.Palette,
                            contentDescription = "Editar Perfil"
                        )
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = LoomIcons.GlobalSettings,
                            contentDescription = "Setup Account"
                        )
                    }
                }
            }

            // AVATAR (Posicionado abajo a la izquierda, sobresaliendo de la caja)
            Surface(
                modifier = Modifier.align(Alignment.BottomStart).offset(x = 16.dp, y = 36.dp).size(72.dp),
                shape = CircleShape,
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.surface),
                color = MaterialTheme.colorScheme.secondary,
                onClick = { if (isEditMode) onAvatarClick() }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val avatarModel = draftAvatarUri ?: profile?.avatarUrl
                    avatarModel?.let { model ->
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(model)
                                .allowHardware(false)
                                .build(),
                            imageLoader = imageLoader,
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
            if (isEditMode) {
                BasicTextField(
                    value = draftDisplayName,
                    onValueChange = onDisplayNameChange,
                    textStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth()
                        // 2. Agregamos el modificador para dibujar la línea punteada
                        /*.drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            val y = size.height - 2.sp.toPx()

                            drawLine(
                                color = Color.Yellow,//textColor.copy(alpha = 0.6f), // Un tono un poco más suave
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth,
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(6f, 6f), // [Largo del punto, Espacio en blanco]
                                    phase = 0f
                                )
                            )
                        }*/,
                    decorationBox = { innerTextField ->
                        Box {
                            if (draftDisplayName.isEmpty()) {
                                Text(
                                    text = "Title",
                                    modifier = Modifier.drawBehind {
                                        val strokeWidth = 2.dp.toPx()
                                        val y = size.height - 2.sp.toPx()

                                        drawLine(
                                            color = Color.Yellow,//textColor.copy(alpha = 0.6f), // Un tono un poco más suave
                                            start = Offset(0f, y),
                                            end = Offset(size.width, y),
                                            strokeWidth = strokeWidth,
                                            pathEffect = PathEffect.dashPathEffect(
                                                intervals = floatArrayOf(6f, 6f), // [Largo del punto, Espacio en blanco]
                                                phase = 0f
                                            )
                                        )
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.DarkGray

                                )
                            }
                            innerTextField()
                        }
                    }
                )
            } else {
                Text(text = profile?.displayName ?: "Cargando...", style = MaterialTheme.typography.titleLarge)
            }
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
        if (isEditMode) {
            BasicTextField(
                value = draftBio,
                onValueChange = onBioChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                decorationBox = { innerTextField ->
                    Box {
                        if (draftBio.isEmpty()) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.DarkGray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        } else {
            Text(
                text = profile?.bio ?: "",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

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

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileHeaderContentPreview() {
    LoomTheme {
        ProfileHeaderContent(
            profile = UserAccountProfile(
                id = "1",
                user = "user1",
                username = "johndoe",
                displayName = "John Doe",
                bio = "Android Developer | UI Enthusiast",
                city = "New York",
                timezone = "EST",
                canBeFollowed = true,
                avatarUrl = null,
                bannerUrl = null,
                locationCoords = null
            ),
            isEditMode = false,
            onEditClick = {},
            onSaveClick = {},
            onCancelClick = {},
            draftDisplayName = "John Doe",
            onDisplayNameChange = {},
            draftBio = "Android Developer | UI Enthusiast",
            onBioChange = {},
            onSettingsClick = {}
        )
    }
}

@Preview(name = "Edit Mode")
@Composable
fun ProfileHeaderContentEditPreview() {
    LoomTheme {
        ProfileHeaderContent(
            profile = UserAccountProfile(
                id = "1",
                user = "user1",
                username = "johndoe",
                displayName = "John Doe",
                bio = "Android Developer | UI Enthusiast",
                city = "New York",
                timezone = "EST",
                canBeFollowed = true,
                avatarUrl = null,
                bannerUrl = null,
                locationCoords = null
            ),
            isEditMode = true,
            onEditClick = {},
            onSaveClick = {},
            onCancelClick = {},
            draftDisplayName = "John Doe",
            onDisplayNameChange = {},
            draftBio = "Android Developer | UI Enthusiast",
            onBioChange = {},
            onSettingsClick = {}
        )
    }
}
