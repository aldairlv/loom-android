package com.loom.feature.posteditor.impl


import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.loom.core.model.data.RowModel
import com.loom.core.model.data.UiEvent
import com.loom.core.model.data.UserAccountProfile
import com.loom.core.ui.editor.EditorScreenContent
import kotlinx.coroutines.flow.SharedFlow
import androidx.compose.ui.layout.positionInWindow
@Composable
fun PostEditorScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    PostEditorScreen(
        userProfile = userProfile,
        onClose = onClose,
        uiEvent = viewModel.uiEvent,
        rowsState = viewModel.rowsState,
        selectedTags = viewModel.selectedTags,
        onAddTag = viewModel::addTag,
        onRemoveTag = viewModel::removeTag,
        pendingFocusBlockId = viewModel.pendingFocusBlockId,
        saveAsDraft = viewModel.saveAsDraft,
        onSaveAsDraftChange = viewModel::onSaveAsDraftChange,
        addMediaFromPicker = viewModel::addMediaFromPicker,
        hasUnsavedChanges = viewModel::hasUnsavedChanges,
        consumeFocusRequest = viewModel::consumeFocusRequest,
        onBlockDropped = viewModel::onBlockDropped,
        onTextChange = viewModel::onTextChange,
        onBackspaceAtStart = viewModel::onBackspaceAtStart,
        guardarBorrador = viewModel::guardarBorrador,
        publicarPost = viewModel::publicarPost,
        onDiscard = viewModel::resetEditor,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PostEditorScreen(
    modifier: Modifier = Modifier,
    userProfile: UserAccountProfile?,
    uiEvent: SharedFlow<UiEvent>,
    rowsState: List<RowModel>,
    selectedTags: List<String>,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    pendingFocusBlockId: String?,
    saveAsDraft: Boolean,
    onSaveAsDraftChange: (Boolean) -> Unit,
    addMediaFromPicker: (Context, List<String>) -> Unit,
    hasUnsavedChanges: () -> Boolean,
    publicarPost: (onSuccess:()-> Unit)-> Unit,
    consumeFocusRequest: ()-> Unit,
    onBlockDropped: (String, String, String, String?) -> Unit,
    onTextChange: (String, String) -> Unit,
    onBackspaceAtStart: (String) -> Unit,
    guardarBorrador: (onSuccess: () -> Unit) -> Unit,
    onDiscard: () -> Unit,
    onClose: () -> Unit
) {
    var showExitDialog by remember { mutableStateOf(false) }
    var showSettingsBottomSheet by remember { mutableStateOf(false) }
    var showTagsBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    val pickMultipleMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val urisAsStrings = uris.map {
                it.toString()
            }
            addMediaFromPicker(context, urisAsStrings)
        }
    }

    val handleCloseAction = {
        if (hasUnsavedChanges()) {
            showExitDialog = true
        } else {
            onDiscard()
            onClose()
        }
    }

    BackHandler(enabled = true) {
        handleCloseAction()
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column(modifier = Modifier.background(Color(0xFF0F172A))) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F172A),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    title = { Text("", fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = handleCloseAction) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    },
                    actions = {
                        Button(
                            onClick = {
                                if (saveAsDraft) {
                                    guardarBorrador { onClose() }
                                } else {
                                    publicarPost { onClose() }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            val buttonText = if (saveAsDraft) "Guardar borrador" else "Publicar"
                            Text(buttonText, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { showSettingsBottomSheet = true }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones")
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (userProfile?.avatarUrl != null) {
                        AsyncImage(
                            model = userProfile.avatarUrl,
                            contentDescription = "Avatar de ${userProfile.displayName}",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEC4899)),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = userProfile?.displayName?.take(2)?.uppercase() ?: "UA"
                            Text(initials, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = userProfile?.displayName ?: "Usuario",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            }
        },
        bottomBar = {
            PostBottomBar(
                selectedTags = selectedTags,
                onAddMediaClick = {
                    pickMultipleMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                },
                onTextFormatClick = { /* Futura acción para darle formato al texto  */ },
                onAddTagsClick = { showTagsBottomSheet = true}
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            EditorScreenContent(
                rows = rowsState,
                pendingFocusBlockId = pendingFocusBlockId,
                onFocusConsumed = consumeFocusRequest,
                onDrop = { blockId, origenId, destinoId, targetBlockId ->
                    onBlockDropped(blockId, origenId, destinoId, targetBlockId)
                },
                onTextChange = { blockId, newText ->
                    onTextChange(blockId, newText)
                },
                onBackspaceAtStart = { blockId ->
                    onBackspaceAtStart(blockId)
                },

                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("¿Qué deseas hacer con tu post?") },
            text = { Text("Tienes cambios sin publicar en este borrador.") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    guardarBorrador { onClose() }
                }) {
                    Text("Guardar Borrador")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onDiscard()
                    onClose()
                }) {
                    Text("Descartar", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    if (showSettingsBottomSheet) {
        PostSettingsBottomSheet(
            saveAsDraft = saveAsDraft,
            onSaveAsDraftChange = onSaveAsDraftChange,
            onDismiss = { showSettingsBottomSheet = false }
        )
    }

    if (showTagsBottomSheet) {
        PostTagsBottomSheet(
            selectedTags = selectedTags,
            onAddTag = onAddTag,
            onRemoveTag = onRemoveTag,
            onDismiss = { showTagsBottomSheet = false }
        )
    }

}

@Composable
fun TagSelectionRow(
    selectedTags: List<String>,
    onAddTagsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onAddTagsClick,
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                text = if (selectedTags.isEmpty()) "# Añade algunas etiquetas" else "#",
                color = Color(0xFF94A3B8),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        selectedTags.forEach { tag ->
            Surface(
                onClick = onAddTagsClick,
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "#$tag",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun ToolBarRow(
    onTextFormatClick: () -> Unit,
    onAddMediaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onTextFormatClick) {
            Icon(
                imageVector = Icons.Default.TextFormat,
                contentDescription = "Formato de texto",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = onAddMediaClick) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Añadir media",
                tint = Color.White
            )
        }
    }
}


@Composable
fun PostBottomBar(
    selectedTags: List<String>,
    onAddMediaClick: () -> Unit,
    onAddTagsClick: () -> Unit,
    modifier: Modifier = Modifier,
    onTextFormatClick: () -> Unit = {}
) {
    Surface(
        color = Color(0xFF1E293B),
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
        ) {
            TagSelectionRow(
                selectedTags = selectedTags,
                onAddTagsClick = onAddTagsClick
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))

            ToolBarRow(
                onTextFormatClick = onTextFormatClick,
                onAddMediaClick = onAddMediaClick
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSettingsBottomSheet(
    saveAsDraft: Boolean,
    onSaveAsDraftChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1E293B),
        contentColor = Color.White,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Opciones de publicación",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text("Hecho", color = Color(0xFF6366F1), fontWeight = FontWeight.Bold)
                }
            }

            ListItem(
                headlineContent = { Text("Guardar borrador", color = Color.White) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8)
                    )
                },
                trailingContent = {
                    RadioButton(
                        selected = saveAsDraft,
                        onClick = { onSaveAsDraftChange(!saveAsDraft) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF6366F1),
                            unselectedColor = Color(0xFF475569)
                        )
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier.clickable { onSaveAsDraftChange(!saveAsDraft) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostTagsBottomSheet(
    selectedTags: List<String>,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var tagInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1E293B),
        contentColor = Color.White,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Añadir Etiquetas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text("Hecho", color = Color(0xFF6366F1), fontWeight = FontWeight.Bold)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedTags.forEach { tag ->
                            Surface(
                                onClick = { onRemoveTag(tag) },
                                color = Color(0xFF334155),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        if (selectedTags.size < 50) {
                            BasicTextField(
                                value = tagInput,
                                onValueChange = { tagInput = it },
                                textStyle = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                cursorBrush = SolidColor(Color(0xFF6366F1)),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (tagInput.isNotBlank()) {
                                            onAddTag(tagInput)
                                            tagInput = ""
                                        }
                                    }
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (tagInput.isEmpty()) {
                                            Text(
                                                text = "Añadir etiquetas...",
                                                color = Color(0xFF94A3B8),
                                                fontSize = 16.sp
                                            )
                                        }
                                        innerTextField()
                                    }
                                },
                                modifier = Modifier.widthIn(min = 120.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Sugerencias",
                    color = Color(0xFF94A3B8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Aquí irán las sugerencias en el futuro
                }
            }
        }
    }
}







@Composable
@Preview
fun CreatePostScreenPreview() {
    PostEditorScreen(
        onClose = {}
    )
}
