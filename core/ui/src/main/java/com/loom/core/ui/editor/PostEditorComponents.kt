package com.loom.core.ui.editor

import android.content.ClipData
import android.content.ClipDescription
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.coerceIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import androidx.compose.ui.text.input.TextFieldValue
import com.loom.core.model.data.Block
import com.loom.core.model.data.ImageBlock
import com.loom.core.model.data.TextBlock
import com.loom.core.model.data.VideoBlock
import com.loom.core.model.data.RowModel
import com.loom.core.ui.post.PostFeedContentBody
import com.loom.core.model.data.PostFeedItem

@Composable
fun BlockComponent(
    block: Block,
    rowOrigenId: String,
    showHint: Boolean,
    onTextChange: (String) -> Unit,
    onBackspaceAtStart: () -> Unit,
    focusRequested: Boolean,
    onFocusConsumed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .dragAndDropSource { _ ->
                DragAndDropTransferData(
                    clipData = ClipData.newPlainText(
                        "block_data",
                        "${block.id}:$rowOrigenId"
                    )
                )
            }
        /*.background(
            color = when (block) {
                is TextBlock -> Color(0xFF1E293B)
                is ImageBlock -> Color(0xFFEC4899)
                is VideoBlock -> Color(0xFF6366F1)
            },
            shape = RoundedCornerShape(4.dp)
        )*/,
        contentAlignment = Alignment.CenterStart
    ) {
        when (block) {
            is TextBlock -> {
                val focusRequester = remember { FocusRequester() }
                val view = LocalView.current
                val density = LocalDensity.current

                // Controlar el foco localmente
                var isFocused by remember { mutableStateOf(false) }

                LaunchedEffect(isFocused) {
                    if (isFocused) {
                        Log.d("EDITOR_DEBUG", "BLOCK: 👍 FOCO ACTIVO en ${block.id}")
                    } else {
                        // Solo logueamos la pérdida si antes tenía foco (para evitar logs de "nacimiento")
                        Log.d("EDITOR_DEBUG", "BLOCK: 🧊 ${block.id} está sin foco (o lo acaba de perder)")
                    }
                }

                // Coordenadas para el Drag
                var blockBoundsInWindow by remember {
                    mutableStateOf<android.graphics.Rect?>(null) }

                // Estado local del TextField
                var fieldValue by remember(block.id) {
                    mutableStateOf(
                        TextFieldValue(
                            text = block.text,
                            selection = TextRange(block.text.length)
                        )
                    )
                }

                LaunchedEffect(block.text) {
                    if (fieldValue.text != block.text) {
                        fieldValue = fieldValue.copy(
                            text = block.text,
                            selection = fieldValue.selection.coerceIn(0, block.text.length)
                        )
                    }
                }

                LaunchedEffect(focusRequested) {
                    if (focusRequested) {
                        focusRequester.requestFocus()
                        fieldValue = TextFieldValue(
                            text = fieldValue.text,
                            selection = TextRange(fieldValue.text.length)
                        )
                        onFocusConsumed()
                    }
                }

                Box(
                    modifier = Modifier.onGloballyPositioned { coords ->
                        val windowOffset = coords.positionInWindow()
                        blockBoundsInWindow = android.graphics.Rect(
                            windowOffset.x.toInt(),
                            windowOffset.y.toInt(),
                            (windowOffset.x + coords.size.width).toInt(),
                            (windowOffset.y + coords.size.height).toInt()
                        )
                    }
                ) {

                    BasicTextField(
                        value = fieldValue,
                        onValueChange = { newValue ->
                            val selectionChanged = fieldValue.selection != newValue.selection
                            if (selectionChanged) {
                                Log.d("EDITOR_DEBUG", "CURSOR: Movimiento en ${block.id}. Posición=${newValue.selection.start}")
                            }
                            Log.d("EDITOR_DEBUG", "BLOCK: ✍️ Escribiendo en ${block.id} (Foco actual: $isFocused)")

                            // Actualizamos primero el estado local para congelar/ocultar el Hint al instante
                            fieldValue = newValue
                            onTextChange(newValue.text)
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        cursorBrush = SolidColor(Color(0xFF6366F1)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            // Escuchamos de manera reactiva el foco del teclado
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            }
                            .pointerInput(block.id, rowOrigenId) {
                                detectDragGesturesAfterLongPress(
                                    onDragStart = { _ ->
                                        // Apagamos el foco al arrastrar para evitar que el hint se quede congelado
                                        isFocused = false

                                        val clipData = ClipData.newPlainText(
                                            "block_data",
                                            "${block.id}:$rowOrigenId"
                                        )

                                        val shadowBuilder = blockBoundsInWindow?.let { bounds ->
                                            object : View.DragShadowBuilder(view) {
                                                override fun onProvideShadowMetrics(
                                                    outShadowSize: android.graphics.Point,
                                                    outShadowTouchPoint: android.graphics.Point
                                                ) {
                                                    val w = bounds.width().coerceAtLeast(1)
                                                    val h = bounds.height().coerceAtLeast(1)
                                                    outShadowSize.set(w, h)
                                                    outShadowTouchPoint.set(w / 2, h / 2)
                                                }

                                                override fun onDrawShadow(canvas: android.graphics.Canvas) {
                                                    canvas.save()
                                                    canvas.translate(
                                                        -bounds.left.toFloat(),
                                                        -bounds.top.toFloat()
                                                    )
                                                    view.draw(canvas)
                                                    canvas.restore()
                                                }
                                            }
                                        } ?: View.DragShadowBuilder(view)

                                        view.startDragAndDrop(
                                            clipData,
                                            shadowBuilder,
                                            null,
                                            View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ
                                        )
                                    },
                                    onDrag = { _, _ -> }
                                )
                            }
                            .onPreviewKeyEvent { keyEvent ->
                                if (keyEvent.type == KeyEventType.KeyDown) {
                                    when (keyEvent.key) {
                                        Key.Backspace -> {
                                            if (fieldValue.selection.collapsed && fieldValue.selection.start == 0) {
                                                onBackspaceAtStart()
                                                return@onPreviewKeyEvent true
                                            }
                                        }
                                        Key.Enter, Key.NumPadEnter -> {
                                            val selectorStart = fieldValue.selection.start
                                            val textoAntesCursor = fieldValue.text.substring(0, selectorStart)
                                            val textoDespuesCursor = fieldValue.text.substring(selectorStart)
                                            onTextChange("$textoAntesCursor\n$textoDespuesCursor")
                                            return@onPreviewKeyEvent true
                                        }
                                    }
                                }
                                false
                            }
                    )
                }
            }
            is ImageBlock -> {
                val aspectRatio = if (block.height > 0) block.width.toFloat() / block.height.toFloat() else 1f
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

                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(block.imgUrl)
                            .allowHardware(false)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Transparent)
                    )
                    if (block.isUploading) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
            is VideoBlock -> {
                val aspectRatio = if (block.height > 0) block.width.toFloat() / block.height.toFloat() else 1f
                val context = LocalContext.current

                // 1. Inicializar y recordar ExoPlayer
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        val mediaItem = MediaItem.fromUri(Uri.parse(block.videoUrl))
                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = false // Inicialmente pausado
                        repeatMode = Player.REPEAT_MODE_ONE // Bucle como un GIF
                    }
                }

                // Estado reactivo para el botón de Play
                var isPlaying by remember { mutableStateOf(false) }

                LaunchedEffect(exoPlayer) {
                    val listener = object : Player.Listener {
                        override fun onIsPlayingChanged(playing: Boolean) {
                            isPlaying = playing
                        }
                    }
                    exoPlayer.addListener(listener)
                }

                // Liberar el reproductor cuando este bloque salga de la pantalla (LazyColumn lo recicla)
                androidx.compose.runtime.DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                // Renderizar el reproductor nativo usando AndroidView dentro de un Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.ui.viewinterop.AndroidView(
                        factory = { ctx ->
                            androidx.media3.ui.PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = false // Desactivar controles nativos para no interceptar gestos
                                setBackgroundColor(android.graphics.Color.BLACK)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay de control personalizado
                    if (!isPlaying) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .clickable {
                                    exoPlayer.playWhenReady = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    } else {
                        // Area táctil para pausar sin controles visibles
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    exoPlayer.playWhenReady = false
                                }
                        )
                    }

                    if (block.isUploading) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowComponent(
    row: RowModel,
    onDropBlock: (blockId: String, origenRowId: String, targetBlockId: String?) -> Unit,
    onTextChange: (blockId: String, newText: String) -> Unit,
    onBackspaceAtStart: (blockId: String) -> Unit,
    pendingFocusBlockId: String? = null,
    onFocusConsumed: () -> Unit = {},
    checkShowHint: (blockId: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    val esFilaVacia = row.blocks.isEmpty()

    if (esFilaVacia) {
        var isDraggingOver by remember { mutableStateOf(false) }
        val callbackVacio = remember(row.id) {
            object : DragAndDropTarget {
                override fun onEntered(event: DragAndDropEvent) { isDraggingOver = true }
                override fun onExited(event: DragAndDropEvent) { isDraggingOver = false }
                override fun onDrop(event: DragAndDropEvent): Boolean {
                    isDraggingOver = false
                    val clipData = event.toAndroidDragEvent().clipData ?: return false
                    if (clipData.itemCount > 0) {
                        val parts = clipData.getItemAt(0).text.toString().split(":")
                        if (parts.size == 2) {
                            onDropBlock(parts[0], parts[1], null)
                            return true
                        }
                    }
                    return false
                }
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                // .height(32.dp)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { ev ->
                        ev.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    },
                    target = callbackVacio
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isDraggingOver) 4.dp else 1.dp)
                    .background(if (isDraggingOver) Color(0xFF00E5FF) else Color.Transparent)
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            row.blocks.forEach { block ->
                key(block.id) {
                    var isDraggingOverBlock by remember { mutableStateOf(false) }
                    val callbackBloque = remember(row.id, block.id) {
                        object : DragAndDropTarget {
                            override fun onEntered(event: DragAndDropEvent) { isDraggingOverBlock = true }
                            override fun onExited(event: DragAndDropEvent) { isDraggingOverBlock = false }
                            override fun onDrop(event: DragAndDropEvent): Boolean {
                                isDraggingOverBlock = false
                                val clipData = event.toAndroidDragEvent().clipData ?: return false
                                if (clipData.itemCount > 0) {
                                    val parts = clipData.getItemAt(0).text.toString().split(":")
                                    if (parts.size == 2) {
                                        onDropBlock(parts[0], parts[1], block.id)
                                        return true
                                    }
                                }
                                return false
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 1.dp)
                            .border(
                                width = if (isDraggingOverBlock) 2.dp else 0.dp,
                                color = if (isDraggingOverBlock) Color(0xFF00E5FF) else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { ev ->
                                    ev.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = callbackBloque
                            )
                    ) {
                        BlockComponent(
                            block = block,
                            rowOrigenId = row.id,
                            showHint = checkShowHint(block.id),
                            onTextChange = { updatedText ->
                                onTextChange(block.id, updatedText)
                            },
                            onBackspaceAtStart = {
                                onBackspaceAtStart(block.id)
                            },
                            focusRequested = pendingFocusBlockId == block.id,
                            onFocusConsumed = onFocusConsumed,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EditorScreenContent(
    rows: List<RowModel>,
    repostPost: PostFeedItem? = null,
    pendingFocusBlockId: String? = null,
    onFocusConsumed: () -> Unit = {},
    onDrop: (blockId: String, origenId: String, destinoId: String, targetBlockId: String?) -> Unit,
    onTextChange: (blockId: String, newText: String) -> Unit,
    onBackspaceAtStart: (blockId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Obtenemos el FocusManager del sistema de Compose
    val focusManager = LocalFocusManager.current
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    // EFECTO NUEVO: Perseguidor de Foco (Auto-scroll)
    LaunchedEffect(pendingFocusBlockId) {
        if (pendingFocusBlockId != null) {
            val index = rows.indexOfFirst { row -> row.blocks.any { it.id == pendingFocusBlockId } }
            val scrollIndex = if (repostPost != null) index + 1 else index
            if (index != -1) {
                Log.d("EDITOR_DEBUG", "SCROLL: Persiguiendo foco -> Scroll hacia índice $scrollIndex")
                listState.animateScrollToItem(scrollIndex)
            }
        }
    }

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        Log.d("EDITOR_DEBUG", "LAZYCOLUMN: Scroll -> FirstIdx=${listState.firstVisibleItemIndex}, Offset=${listState.firstVisibleItemScrollOffset}")
    }

    // Simplificamos eliminando el 'remember' rígido para que evalúe el estado en tiempo real cada vez que muta 'rows'
    val filasReales = rows.filter { !it.id.startsWith("sensor-") && it.id != "row-sensor-intercalado" }
    val esUnicaFilaSola = filasReales.size == 1
    val idDeLaUnicaFila = filasReales.firstOrNull()?.id


    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp)
            // Detectamos toques en el fondo para limpiar el foco activo
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
    ) {
        if (repostPost != null) {
            android.util.Log.d("LOOM_REPOST_DEBUG", "EditorScreenContent: repostPost is NOT null. ID=${repostPost.id}")
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                ) {
                    PostFeedContentBody(postFeed = repostPost)
                }
            }
        } else {
            android.util.Log.d("LOOM_REPOST_DEBUG", "EditorScreenContent: repostPost is null")
        }

        // creo si es un repost con comentario tendria que ser un item aqui una columna
        // con borde oscuro difuminado para diferencia que no es un contenido nuevo.
        // dentro de esa columna se llamaria a PostFeedContentBody para renderizar el post
        items(
            items = rows,
            key = { row -> row.id }
        ) { row ->
            // CONDICIÓN UNIFICADA: Si el ID empieza con "sensor-", se trata de un sensor intermedio o global
            val esSensor = row.id.startsWith("sensor-")

            if (esSensor) {
                RowComponent(
                    row = row.copy(blocks = emptyList()), // Forzamos comportamiento visual de sensor reactivo
                    onDropBlock = { blockId, origenId, targetBlockId ->
                        onDrop(blockId, origenId, row.id, targetBlockId)
                    },
                    onTextChange = onTextChange,
                    onBackspaceAtStart = {}, // Los sensores no emiten backspace
                    pendingFocusBlockId = pendingFocusBlockId,
                    onFocusConsumed = onFocusConsumed,
                    checkShowHint = {false},
                    modifier = Modifier.height(6.dp)
                    //modifier = Modifier.height(16.dp) // Reducimos espacio de padding de sensores intercalados
                )
            } else {
                RowComponent(
                    row = row,
                    onDropBlock = { blockId, origenId, targetBlockId ->
                        onDrop(blockId, origenId, row.id, targetBlockId)
                    },
                    onTextChange = onTextChange,
                    onBackspaceAtStart = onBackspaceAtStart,
                    pendingFocusBlockId = pendingFocusBlockId,
                    onFocusConsumed = onFocusConsumed,
                    checkShowHint = { _ ->
                        val resultado = esUnicaFilaSola && row.id == idDeLaUnicaFila
                        Log.d("HINT_DEBUG_SCREEN", "Evaluando RowID: ${row.id} | esUnicaFila: $esUnicaFilaSola | DeberiaMostrarHint: $resultado")
                        resultado
                    },
                    //modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

// TODO: Arreglar el preview
/*
@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun EditorScreenPreview() {
    var mockRows by remember {
        mutableStateOf(
            listOf(
                RowModel(id = "sensor-top"),
                RowModel(
                    id = "row-text-1",
                    blocks = listOf(TextBlock(id = "b1", text = "Encabezado del post"))
                ),
                RowModel(id = "sensor-1"),
                RowModel(
                    id = "row-images-1",
                    blocks = listOf(
                        ImageBlock(id = "b2", imgUrl = "url1", mimeType = "image/jpg", width = 200, height = 150),
                        ImageBlock(id = "b3", imgUrl = "url2", mimeType = "image/jpg", width = 300, height = 180)
                    )
                ),
                RowModel(id = "sensor-2"),
                RowModel(
                    id = "row-text-2",
                    blocks = listOf(TextBlock(id = "b4", text = "Pie de página informativo"))
                ),
                RowModel(id = "sensor-bottom")
            )
        )
    }

    Box(modifier = Modifier.padding(8.dp)) {
        var pendingFocusBlockId by remember { mutableStateOf<String?>(null) }

        EditorScreenContent(
            rows = mockRows,
            pendingFocusBlockId = pendingFocusBlockId,
            onFocusConsumed = { pendingFocusBlockId = null },
            onDrop = { blockId, origenId, destinoId, targetBlockId ->
                mockRows = calcularNuevaLista(mockRows, blockId, origenId, destinoId, targetBlockId)
            },
            onTextChange = { blockId, newText ->
                val result = actualizarTexto(mockRows, blockId, newText)
                mockRows = result.rows
                result.focusBlockId?.let { pendingFocusBlockId = it }
            },
            onBackspaceAtStart = {}
        )
    }
}

*/