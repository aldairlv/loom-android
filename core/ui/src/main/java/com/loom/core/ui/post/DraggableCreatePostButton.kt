package com.loom.core.ui.post

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.theme.LoomTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun DraggableCreatePostButton(
    onClick: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp, end = 10.dp), // Ajuste para que no tape el centro del bottom bar
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .size(56.dp)
                .shadow(6.dp, CircleShape)
                .background(Color(0xFF03A9F4), CircleShape)
                .pointerInput(Unit) {
                    // detectDragGestures tiene parámetros nombrados: onDragStart, onDragEnd, onDragCancel, onDrag
                    detectDragGestures(
                        onDragEnd = {
                            scope.launch {
                                launch { offsetX.animateTo(0f, spring(Spring.DampingRatioLowBouncy)) }
                                launch { offsetY.animateTo(0f, spring(Spring.DampingRatioLowBouncy)) }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            // IMPORTANTE: Consumir el evento para que no se mueva el scroll de fondo
                            change.consume()
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                                offsetY.snapTo(offsetY.value + dragAmount.y)
                            }
                        }
                    )
                }
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Crear Post",
                tint = Color.White
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, showSystemUi = false)
@Preview(name = "Dark Mode", showBackground = true, showSystemUi = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DraggableCreatePostButtonPreview() {
    LoomTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DraggableCreatePostButton {

            }
        }
    }
}