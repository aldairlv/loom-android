package com.loom.feature.posteditor.impl

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.HomeRepository
import com.loom.core.data.repository.UserRepository
import com.loom.core.data.repository.UserDataRepository
import com.loom.core.model.data.Block
import com.loom.core.model.data.ImageBlock
import com.loom.core.model.data.RowModel
import com.loom.core.model.data.TextBlock
import com.loom.core.model.data.UiEvent
import com.loom.core.model.data.UserAccountProfile
import com.loom.core.model.data.VideoBlock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val userProfile: StateFlow<UserAccountProfile?> = userDataRepository.userData
        .map { it.userId }
        .flatMapLatest { userId ->
            if (userId.isEmpty()) {
                kotlinx.coroutines.flow.flowOf(null)
            } else {
                userRepository.getUserAccountProfileByUserId(userId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            userDataRepository.userData.map { it.userId }.collect { userId ->
                if (userId.isNotEmpty()) {
                    userRepository.syncMyProfile()
                }
            }
        }
    }

    var rowsState by mutableStateOf<List<RowModel>>(emptyList())
        private set

    var saveAsDraft by mutableStateOf(false)
        private set

    var selectedTags by mutableStateOf<List<String>>(emptyList())
        private set

    fun onSaveAsDraftChange(value: Boolean) {
        saveAsDraft = value
    }

    fun addTag(tag: String) {
        val trimmedTag = tag.trim().lowercase().removePrefix("#")
        if (trimmedTag.isNotEmpty() && selectedTags.size < 50 && !selectedTags.contains(trimmedTag)) {
            selectedTags = selectedTags + trimmedTag
        }
    }

    fun removeTag(tag: String) {
        selectedTags = selectedTags - tag
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent



    init {
        rowsState = ensureTrailingRow(emptyList())
    }

    var pendingFocusBlockId by mutableStateOf<String?>(null)
        private set

    fun requestFocusOnBlock(blockId: String) {
        pendingFocusBlockId = blockId
    }

    fun consumeFocusRequest() {
        pendingFocusBlockId = null
    }

    private fun getCantidadTotalMedia(): Int {
        return rowsState.flatMap { it.blocks }.count { it is ImageBlock || it is VideoBlock }
    }

    fun addMediaFromPicker(context: Context, uris: List<String>) {
        val mediaActual = getCantidadTotalMedia()

        if (mediaActual + uris.size > 10) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackbar("Máximo 10 archivos multimedia por post"))
            }
            return
        }

        if (uris.isEmpty()) return

        val contentResolver = context.contentResolver
        val objetosMedia = uris.map { uriString ->
            val uri = Uri.parse(uriString)
            val mimeType = contentResolver.getType(uri) ?: ""

            if (mimeType.startsWith("video/")) {
                VideoBlock(videoUrl = uriString, mimeType = mimeType, width = 1920, height = 1080, isUploading = true)
            } else {
                var realWidth = 300
                var realHeight = 200
                try {
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                        BitmapFactory.decodeStream(inputStream, null, options)
                        if (options.outWidth > 0 && options.outHeight > 0) {
                            realWidth = options.outWidth
                            realHeight = options.outHeight
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                ImageBlock(
                    imgUrl = uriString,
                    mimeType = mimeType,
                    width = realWidth,
                    height = realHeight,
                    originalWidth = realWidth,
                    originalHeight = realHeight,
                    isUploading = true
                )
            }
        }

        // Trigger uploads for each media
        objetosMedia.forEach { block ->
            val uriString = when(block) {
                is ImageBlock -> block.imgUrl
                is VideoBlock -> block.videoUrl
                else -> ""
            }
            if (uriString.isNotEmpty()) {
                uploadMediaInternal(context, block.id, uriString)
            }
        }

        val tandas = mutableListOf<List<Block>>()
        val tandaActualIdem = mutableListOf<Block>()

        for (item in objetosMedia) {
            if (item is VideoBlock) {
                if (tandaActualIdem.isNotEmpty()) {
                    tandas.add(tandaActualIdem.toList())
                    tandaActualIdem.clear()
                }
                tandas.add(listOf(item))
            } else {
                tandaActualIdem.add(item)
            }
        }
        if (tandaActualIdem.isNotEmpty()) {
            tandas.add(tandaActualIdem.toList())
        }

        val timestamp = System.currentTimeMillis()
        val nuevasFilasConSensores = mutableListOf<RowModel>()

        tandas.forEachIndexed { indexTanda, tanda ->
            val distribucionDeFilas: List<List<Block>> = when {
                tanda.size == 1 && tanda[0] is VideoBlock -> listOf(listOf(tanda[0]))
                tanda.size == 1 -> listOf(listOf(tanda[0]))
                tanda.size == 2 -> listOf(listOf(tanda[0], tanda[1]))
                tanda.size == 3 -> listOf(listOf(tanda[0], tanda[1]), listOf(tanda[2]))
                tanda.size == 4 -> listOf(listOf(tanda[0], tanda[1]), listOf(tanda[2], tanda[3]))
                tanda.size == 5 -> listOf(listOf(tanda[0], tanda[1]), listOf(tanda[2]), listOf(tanda[3], tanda[4]))
                tanda.size == 6 -> listOf(listOf(tanda[0], tanda[1]), listOf(tanda[2], tanda[3]), listOf(tanda[4], tanda[5]))
                tanda.size == 7 -> listOf(listOf(tanda[0], tanda[1]), listOf(tanda[2], tanda[3], tanda[4]), listOf(tanda[5], tanda[6]))
                tanda.size == 8 -> listOf(listOf(tanda[0], tanda[1], tanda[2]), listOf(tanda[3], tanda[4]), listOf(tanda[5], tanda[6], tanda[7]))
                tanda.size == 9 -> listOf(listOf(tanda[0], tanda[1], tanda[2]), listOf(tanda[3], tanda[4], tanda[5]), listOf(tanda[6], tanda[7], tanda[8]))
                tanda.size == 10 -> listOf(listOf(tanda[0], tanda[1], tanda[2]), listOf(tanda[3], tanda[4]), listOf(tanda[5], tanda[6], tanda[7]), listOf(tanda[8], tanda[9]))
                else -> emptyList()
            }

            distribucionDeFilas.forEachIndexed { indexFila, bloquesDeFila ->
                val idFilaEstable = "row-media-${bloquesDeFila.first().id}"
                nuevasFilasConSensores.add(
                    RowModel(
                        id = idFilaEstable,
                        blocks = bloquesDeFila
                    )
                )
                nuevasFilasConSensores.add(
                    RowModel(id = "sensor-after-$idFilaEstable")
                )
            }
        }

        val listaModificada = rowsState.toMutableList()
        if (listaModificada.lastOrNull()?.id == "sensor-bottom")
            listaModificada.removeAt(listaModificada.size - 1)

        val posicionInsercion = (listaModificada.size - 1).coerceAtLeast(0)
        listaModificada.addAll(posicionInsercion, nuevasFilasConSensores)

        val listaAsegurada = ensureTrailingRow(listaModificada)
        rowsState = normalizarAspectRatiosFila(listaAsegurada)
    }

    fun onBlockDropped(blockId: String, rowOrigenId: String, rowDestinoId: String, targetBlockId: String?) {
        val nuevaLista = calcularNuevaLista(rowsState, blockId, rowOrigenId, rowDestinoId, targetBlockId)
        val listaAsegurada = ensureTrailingRow(nuevaLista)
        rowsState = normalizarAspectRatiosFila(listaAsegurada)
    }

    fun onTextChange(blockId: String, newText: String) {

        val result = actualizarTexto(
            rowsState,
            blockId,
            newText
        )

        rowsState = result.rows

        result.focusBlockId?.let {
            requestFocusOnBlock(it)
        }
    }

    fun hasUnsavedChanges(): Boolean {
        if (selectedTags.isNotEmpty()) return true

        val nonSensorRows = rowsState.filter { !it.id.startsWith("sensor-") && it.id != "row-sensor-intercalado" }

        if (nonSensorRows.size > 1) return true
        if (nonSensorRows.isEmpty()) return false

        val firstRow = nonSensorRows.first()
        if (firstRow.blocks.size > 1) return true
        if (firstRow.blocks.isEmpty()) return false

        val firstBlock = firstRow.blocks.first()
        return when (firstBlock) {
            is TextBlock -> firstBlock.text.isNotEmpty()
            else -> true
        }
    }

    fun resetEditor() {
        rowsState = ensureTrailingRow(emptyList())
        selectedTags = emptyList()
        saveAsDraft = false
        pendingFocusBlockId = null
    }

    fun publicarPost(onSuccess: () -> Unit) {
        val isUploading = rowsState.flatMap { it.blocks }.any { 
            (it is ImageBlock && it.isUploading) || (it is VideoBlock && it.isUploading) 
        }
        
        if (isUploading) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowSnackbar("Espera a que termine la subida de archivos"))
            }
            return
        }

        viewModelScope.launch {
            try {
                val status = if (saveAsDraft) "draft" else "published"
                homeRepository.createPost(
                    status = status,
                    tags = selectedTags,
                    rows = rowsState
                )
                resetEditor()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.emit(UiEvent.ShowSnackbar("Error al publicar el post"))
            }
        }
    }

    fun guardarBorrador(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                homeRepository.createPost(
                    status = "draft",
                    tags = selectedTags,
                    rows = rowsState
                )
                resetEditor()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.emit(UiEvent.ShowSnackbar("Error al guardar borrador"))
            }
        }
    }

    fun onBackspaceAtStart(blockId: String) {
        val resultado = procesarBackspaceAlInicio(rowsState, blockId)
        rowsState = resultado.rows

        resultado.focusBlockId?.let { idAEnfocar ->
            requestFocusOnBlock(idAEnfocar)
        }
    }

    private fun uploadMediaInternal(context: Context, blockId: String, uriString: String) {
        viewModelScope.launch {
            try {
                val uri = Uri.parse(uriString)
                val contentResolver = context.contentResolver
                val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return@launch
                val mimeType = contentResolver.getType(uri) ?: "image/jpeg"

                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                val fileName = (uri.lastPathSegment ?: "file").let { name ->
                    if (extension != null && !name.endsWith(".$extension", ignoreCase = true)) {
                        "$name.$extension"
                    } else {
                        name
                    }
                }

                val postMedia = homeRepository.uploadMedia(fileName, mimeType, bytes)

                rowsState = rowsState.map { row ->
                    row.copy(
                        blocks = row.blocks.map { block ->
                            if (block.id == blockId) {
                                when (block) {
                                    is ImageBlock -> block.copy(
                                        imgUrl = postMedia.url,
                                        width = postMedia.width,
                                        height = postMedia.height,
                                        originalWidth = postMedia.width,
                                        originalHeight = postMedia.height,
                                        isUploading = false,
                                        backendId = postMedia.id
                                    )
                                    is VideoBlock -> block.copy(
                                        videoUrl = postMedia.url,
                                        width = postMedia.width,
                                        height = postMedia.height,
                                        originalWidth = postMedia.width,
                                        originalHeight = postMedia.height,
                                        isUploading = false,
                                        backendId = postMedia.id
                                    )
                                    else -> block
                                }
                            } else {
                                block
                            }
                        }
                    )
                }
                rowsState = normalizarAspectRatiosFila(rowsState)

            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.emit(UiEvent.ShowSnackbar("Error al subir archivo"))
                rowsState = rowsState.map { row ->
                    row.copy(
                        blocks = row.blocks.map { block ->
                            if (block.id == blockId) {
                                when (block) {
                                    is ImageBlock -> block.copy(isUploading = false)
                                    is VideoBlock -> block.copy(isUploading = false)
                                    else -> block
                                }
                            } else {
                                block
                            }
                        }
                    )
                }
            }
        }
    }
}