package com.loom.core.ui.mocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.ui.AuthButton

// Definimos un modelo de datos para el Preview
data class AuthButtonState(
    val text: String,
    val isGoogle: Boolean
)

class AuthButtonPreviewParameterProvider : PreviewParameterProvider<AuthButtonState> {
    override val values = sequenceOf(
        AuthButtonState("Continua con Google", isGoogle = true),
        AuthButtonState("Continua con tu correo", isGoogle = false)
    )
}

