package com.loom.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.designsystem.theme.LoomTheme

@Composable
fun LoomIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    iconSize: Dp = 24.dp,
    padding: Dp = 8.dp, // Aquí controlas el espacio extra alrededor del icono
    rippleRadius: Dp = 20.dp // Controla el tamaño del destello circular
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = false,
                    radius = rippleRadius
                )
            )
            .padding(padding), // El padding va DESPUÉS del clickable para que el área táctil sea cómoda
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@ThemePreviews
@Composable
fun LoomIconButtonPreview() {
    LoomTheme {
        LoomBackground {
            LoomIconButton(
                icon = LoomIcons.Like,
                contentDescription = null,
                onClick = {}
            )
        }
    }
}
