package com.loom.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.ui.mocks.AuthButtonPreviewParameterProvider
import com.loom.core.ui.mocks.AuthButtonState

@Composable
fun AuthButton(
    text: String,
    icon: ImageVector, // <--- Acepta ImageVector
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFFEFEFEF),
    contentColor: Color = Color.Black
) {
    AuthButton(
        text = text,
        icon = rememberVectorPainter(icon), // Convierte y llama a la versión original
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    )
}
@Composable
fun AuthButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFFEFEFEF), // Gris claro similar a tu imagen
    contentColor: Color = Color.Black
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp), // Altura estándar para botones de login
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified // Importante para que el logo de Google mantenga sus colores
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true, name = "AuthButtons Preview")
@Composable
fun AuthButtonPreview(
    @PreviewParameter(AuthButtonPreviewParameterProvider::class) state: AuthButtonState
) {
    // Reemplaza 'LoomTheme' por el nombre de tu tema real
    LoomTheme {
        Box(Modifier.padding(16.dp)) {
            AuthButton(
                text = state.text,
                icon = if (state.isGoogle) {
                    // Asegúrate de tener el recurso ic_google en res/drawable
                    painterResource(id = android.R.drawable.ic_menu_compass) // Placeholder
                } else {
                    // Icono de carta/email
                    painterResource(id = android.R.drawable.ic_dialog_email)
                },
                onClick = { /* Acción */ }
            )
        }
    }
}