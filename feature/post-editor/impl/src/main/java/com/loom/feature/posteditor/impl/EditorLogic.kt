package com.loom.feature.posteditor.impl

import android.util.Log
import com.loom.core.model.data.Block
import com.loom.core.model.data.ImageBlock
import com.loom.core.model.data.RowModel
import com.loom.core.model.data.TextBlock
import com.loom.core.model.data.VideoBlock
import java.util.UUID

private fun generarIdUnico(prefijo: String): String {
    val id = "$prefijo-${UUID.randomUUID().toString().take(4)}"
    Log.d("EDITOR_DEBUG", "LOGIC: Generando ID único: $id")
    return id
}
private fun puedeInsertarseEnFila(
    fila: RowModel,
    bloque: Block
): Boolean {
    if (fila.blocks.isEmpty()) return true

    return when (bloque) {
        is TextBlock -> false
        is ImageBlock, is VideoBlock -> fila.blocks.none { it is TextBlock }
    }
}

fun normalizarEstructuraEditor(listaActual: List<RowModel>): List<RowModel> {
    Log.d("EDITOR_DEBUG", "LOGIC: Iniciando normalización. Filas entrada: ${listaActual.size}")

    val filasContenido = listaActual.filter { row ->
        !row.id.startsWith("sensor-") && row.id != "row-sensor-intercalado"
    }.toMutableList()

    val filasProcesadas = mutableListOf<RowModel>()
    filasContenido.forEach { row ->
        val textBlock = row.blocks.firstOrNull() as? TextBlock
        if (textBlock != null && textBlock.text.contains("\n")) {
            val lineas = textBlock.text.split("\n")
            lineas.forEachIndexed { index, linea ->
                filasProcesadas.add(
                    RowModel(
                        id = if (index == 0) row.id else generarIdUnico(prefijo = "row-text-split"),
                        blocks = listOf(
                            TextBlock(
                                id = if (index == 0) textBlock.id else generarIdUnico("block-text-split"),
                                text = linea
                            )
                        )
                    )
                )
            }
        } else {
            filasProcesadas.add(row)
        }
    }

    val ultimaFila = filasProcesadas.lastOrNull()
    val ultimoBloque = ultimaFila?.blocks?.firstOrNull()

    val lastIsMedia = ultimoBloque is ImageBlock || ultimoBloque is VideoBlock
    val lastIsTextNotEmpty = ultimoBloque is TextBlock && ultimoBloque.text.isNotEmpty()

    val necesitaTrailing = ultimaFila == null ||
            ultimaFila.blocks.isEmpty() ||
            lastIsMedia ||
            lastIsTextNotEmpty


    if (necesitaTrailing) {
        val motivo = when {
            ultimaFila == null -> "Lista vacía"
            ultimaFila.blocks.isEmpty() -> "Fila sin bloques"
            lastIsMedia -> "Última fila es Multimedia"
            lastIsTextNotEmpty -> "Última fila de texto ya tiene contenido: '${(ultimoBloque as TextBlock).text}'"
            else -> "Desconocido"
        }
        Log.d("EDITOR_DEBUG", "LOGIC: ⚡ Detectada necesidad de Trailing Row. Motivo: $motivo")

        filasProcesadas.add(
            RowModel(
                id = generarIdUnico("row-text-trailing"),
                blocks = listOf(TextBlock(id = generarIdUnico("block-text-trailing"), text = ""))
            )
        )
    }

    val listaFinal = mutableListOf<RowModel>()

    listaFinal.add(RowModel(id = "sensor-top"))

    filasProcesadas.forEachIndexed { index, row ->
        listaFinal.add(row)
        if (index < filasProcesadas.lastIndex) {
            listaFinal.add(RowModel(id = "sensor-intermedio-${row.id}-${filasProcesadas[index + 1].id}"))
        }
    }

    listaFinal.add(RowModel(id = "sensor-bottom"))

    return listaFinal
}

fun ensureTrailingRow(listaActual: List<RowModel>): List<RowModel> {
    return normalizarEstructuraEditor(listaActual)
}

fun calcularNuevaLista(
    listaOriginal: List<RowModel>,
    blockId: String,
    rowOrigenId: String,
    rowDestinoId: String,
    targetBlockId: String?
): List<RowModel> {
    val indexOrigenInicial = listaOriginal.indexOfFirst { it.id == rowOrigenId }
    var indexDestinoInicial = listaOriginal.indexOfFirst { it.id == rowDestinoId }

    if (indexOrigenInicial == -1 || indexDestinoInicial == -1) return listaOriginal

    if (rowOrigenId == rowDestinoId) {
        val fila = listaOriginal[indexOrigenInicial]
        if (fila.blocks.any { it is TextBlock }) return listaOriginal

        val bloques = fila.blocks.toMutableList()
        val srcIndex = bloques.indexOfFirst { it.id == blockId }
        if (srcIndex == -1) return listaOriginal
        val bloque = bloques.removeAt(srcIndex)

        val targetIndex = if (targetBlockId != null) {
            bloques.indexOfFirst { it.id == targetBlockId }.let { if (it == -1) bloques.size else it }
        } else {
            bloques.size
        }
        bloques.add(targetIndex, bloque)

        val lista = listaOriginal.toMutableList()
        lista[indexOrigenInicial] = fila.copy(blocks = bloques)
        return normalizarEstructuraEditor(lista)
    }

    val filaOrigen = listaOriginal[indexOrigenInicial]
    val bloque = filaOrigen.blocks.find { it.id == blockId } ?: return listaOriginal

    val filaDestino = listaOriginal[indexDestinoInicial]
    if (!puedeInsertarseEnFila(filaDestino, bloque)) return listaOriginal

    val esMovimientoHaciaAbajo = indexDestinoInicial > indexOrigenInicial
    val lista = listaOriginal.toMutableList()


    val idxOrigenActual = lista.indexOfFirst { it.id == rowOrigenId }
    val bloquesSinBloque = lista[idxOrigenActual].blocks.filter { it.id != blockId }
    lista[idxOrigenActual] = lista[idxOrigenActual].copy(blocks = bloquesSinBloque)
    val origenQuedaVacio = bloquesSinBloque.isEmpty()

    val idxDestinoActual = lista.indexOfFirst { it.id == rowDestinoId }
    val filaDestinoActual = lista[idxDestinoActual]
    val destinoEstaOcupado = filaDestinoActual.blocks.isNotEmpty()

    val targetIndexCalculado = if (targetBlockId != null) {
        filaDestinoActual.blocks.indexOfFirst { it.id == targetBlockId }.let { if (it == -1) filaDestinoActual.blocks.size else it }
    } else {
        filaDestinoActual.blocks.size
    }

    if (esMovimientoHaciaAbajo) {
        escenarioA(lista, bloque, rowOrigenId, rowDestinoId, destinoEstaOcupado, origenQuedaVacio, targetIndexCalculado)
    } else {
        escenarioB(lista, bloque, rowOrigenId, rowDestinoId, destinoEstaOcupado, origenQuedaVacio, targetIndexCalculado)
    }

    return normalizarEstructuraEditor(lista)
}

private fun escenarioA(lista: MutableList<RowModel>, bloque: Block, origenId: String, destinoId: String, destinoOcupado: Boolean, origenQuedaVacio: Boolean, targetIndex: Int) {
    if (destinoOcupado) {
        val idxDestino = lista.indexOfFirst { it.id == destinoId }
        val blocks = lista[idxDestino].blocks.toMutableList()
        blocks.add(targetIndex.coerceIn(0, blocks.size), bloque)
        lista[idxDestino] = lista[idxDestino].copy(blocks = blocks)
        if (origenQuedaVacio) {
            val idxOrigen = lista.indexOfFirst { it.id == origenId }
            lista.removeAt(idxOrigen)
        }
    } else {
        val nuevaFilaConBloque = RowModel(id = generarIdUnico("row-gen-media"), blocks = listOf(bloque))
        val idxDestino = lista.indexOfFirst { it.id == destinoId }
        lista.add(idxDestino + 1, nuevaFilaConBloque)
        if (origenQuedaVacio) {
            val idxOrigen = lista.indexOfFirst { it.id == origenId }
            lista.removeAt(idxOrigen)
        }
    }
}

private fun escenarioB(lista: MutableList<RowModel>, bloque: Block, origenId: String, destinoId: String, destinoOcupado: Boolean, origenQuedaVacio: Boolean, targetIndex: Int) {
    if (destinoOcupado) {
        val idxDestino = lista.indexOfFirst { it.id == destinoId }
        val blocks = lista[idxDestino].blocks.toMutableList()
        blocks.add(targetIndex.coerceIn(0, blocks.size), bloque)
        lista[idxDestino] = lista[idxDestino].copy(blocks = blocks)
        if (origenQuedaVacio) {
            val idxOrigen = lista.indexOfFirst { it.id == origenId }
            lista.removeAt(idxOrigen)
        }
    } else {
        val nuevaFilaConBloque = RowModel(id = generarIdUnico("row-gen-media"), blocks = listOf(bloque))
        val idxDestino = lista.indexOfFirst { it.id == destinoId }
        lista.add(idxDestino, nuevaFilaConBloque)
        if (origenQuedaVacio) {
            val idxOrigen = lista.indexOfFirst { it.id == origenId }
            lista.removeAt(idxOrigen)
        }
    }
}

data class TextUpdateResult(
    val rows: List<RowModel>,
    val focusBlockId: String? = null
)

fun actualizarTexto(
    rows: List<RowModel>,
    blockId: String,
    newText: String
): TextUpdateResult {
    Log.d("EDITOR_DEBUG", "LOGIC: actualizarTexto en $blockId. NewText: '$newText'")
    if ("\n" !in newText) {
        return TextUpdateResult(
            rows = normalizarEstructuraEditor(
                rows.map { row ->
                    row.copy(
                        blocks = row.blocks.map { block ->
                            if (block.id == blockId && block is TextBlock) {
                                block.copy(text = newText)
                            } else {
                                block
                            }
                        }
                    )
                }
            )
        )
    }

    val partes = newText.split("\n", limit = 2)

    val textoActual = partes[0]
    val textoNuevaFila = partes[1]

    val nuevaFilaId = generarIdUnico("row-text-split")
    val nuevoBlockId = generarIdUnico("block-text-split")

    val resultado = mutableListOf<RowModel>()

    rows.forEach { row ->

        val bloqueObjetivo = row.blocks.firstOrNull {
            it.id == blockId
        } as? TextBlock

        if (bloqueObjetivo == null) {
            resultado += row
            return@forEach
        }

        resultado += row.copy(
            blocks = listOf(
                bloqueObjetivo.copy(text = textoActual)
            )
        )

        resultado += RowModel(
            id = nuevaFilaId,
            blocks = listOf(
                TextBlock(
                    id = nuevoBlockId,
                    text = textoNuevaFila
                )
            )
        )
    }

    return TextUpdateResult(
        rows = normalizarEstructuraEditor(resultado),
        focusBlockId = nuevoBlockId
    )
}

fun normalizarAspectRatiosFila(rows: List<RowModel>): List<RowModel> {
    return rows.map { row ->
        val mediaBlocks = row.blocks.filter { it is ImageBlock || it is VideoBlock }

        if (mediaBlocks.size <= 1) {
            val bloquesRestaurados = row.blocks.map { block ->
                when (block) {
                    is ImageBlock -> block.copy(width = block.originalWidth, height = block.originalHeight)
                    is VideoBlock -> block.copy(width = block.originalWidth, height = block.originalHeight)
                    else -> block
                }
            }
            row.copy(blocks = bloquesRestaurados)
        } else {
            val maxAspectRatio = mediaBlocks.map { block ->
                val w = if (block is ImageBlock) block.originalWidth else (block as VideoBlock).originalWidth
                val h = if (block is ImageBlock) block.originalHeight else (block as VideoBlock).originalHeight
                if (h > 0) w.toFloat() / h.toFloat() else 1f
            }.maxOrNull() ?: 1f

            val bloquesNormalizados = row.blocks.map { block ->
                when (block) {
                    is ImageBlock -> {
                        val nuevoAlto = (block.width / maxAspectRatio).toInt()
                        block.copy(height = nuevoAlto)
                    }
                    is VideoBlock -> {
                        val nuevoAlto = (block.width / maxAspectRatio).toInt()
                        block.copy(height = nuevoAlto)
                    }
                    else -> block
                }
            }
            row.copy(blocks = bloquesNormalizados)
        }
    }
}


fun procesarBackspaceAlInicio(
    rows: List<RowModel>,
    blockId: String
): TextUpdateResult {
    Log.d("EDITOR_DEBUG", "LOGIC: procesarBackspaceAlInicio en $blockId")
    val filasContenido = rows.filter { !it.id.startsWith("sensor-") }

    val indexFilaActual = filasContenido.indexOfFirst { row -> row.blocks.any { it.id == blockId } }

    if (indexFilaActual <= 0) return TextUpdateResult(rows = rows)

    val filaActual = filasContenido[indexFilaActual]
    val filaArriba = filasContenido[indexFilaActual - 1]

    val bloqueActual = filaActual.blocks.firstOrNull { it.id == blockId } as? TextBlock ?: return TextUpdateResult(rows = rows)
    val bloqueArriba = filaArriba.blocks.lastOrNull()

    val resultadoFilasModificadas = rows.toMutableList()

    if (bloqueArriba is TextBlock) {
        val textoFusionado = bloqueArriba.text + bloqueActual.text

        val filaArribaModificada = filaArriba.copy(
            blocks = filaArriba.blocks.map {
                if (it.id == bloqueArriba.id) (it as TextBlock).copy(text = textoFusionado) else it
            }
        )

        val idxRealArriba = resultadoFilasModificadas.indexOfFirst { it.id == filaArriba.id }
        val idxRealActual = resultadoFilasModificadas.indexOfFirst { it.id == filaActual.id }

        resultadoFilasModificadas[idxRealArriba] = filaArribaModificada

        if (idxRealActual != -1) {
            resultadoFilasModificadas.removeAt(idxRealActual)
        }

        return TextUpdateResult(
            rows = normalizarEstructuraEditor(resultadoFilasModificadas),
            focusBlockId = bloqueArriba.id
        )

    } else {
        if (bloqueActual.text.isEmpty()) {
            val idxRealActual = resultadoFilasModificadas.indexOfFirst { it.id == filaActual.id }
            if (idxRealActual != -1) {
                resultadoFilasModificadas.removeAt(idxRealActual)
            }

            val ultimoTextoArriba = filasContenido.take(indexFilaActual)
                .flatMap { it.blocks }
                .lastOrNull { it is TextBlock }

            return TextUpdateResult(
                rows = normalizarEstructuraEditor(resultadoFilasModificadas),
                focusBlockId = ultimoTextoArriba?.id
            )
        }
    }

    return TextUpdateResult(rows = rows)
}