package com.loom.core.ui.mocks

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

// 1. Definimos el Provider para pasar estados iniciales al Preview
class PasswordTextFieldPreviewParameterProvider : PreviewParameterProvider<TextFieldState> {
    override val values = sequenceOf(
        TextFieldState(""),            // Vacío
        TextFieldState("mi_password"), // Con texto
    )
}