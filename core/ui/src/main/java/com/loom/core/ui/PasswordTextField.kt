package com.loom.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.ui.mocks.PasswordTextFieldPreviewParameterProvider

/*
@Composable
fun PasswordTextField(
    state: TextFieldState = remember { TextFieldState() },
    modifier: Modifier = Modifier,
    hint: String = "" // Añadimos hint para que el usuario sepa qué escribir
) {

    //val state = remember { TextFieldState() }
    var showPassword by remember { mutableStateOf(false) }
    BasicSecureTextField(
        state = state,
        textObfuscationMode =
            if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .padding(6.dp),
        decorator = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                ) {
                    innerTextField()
                }
                Icon(
                    imageVector = if (showPassword) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = "Toggle password visibility",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .requiredSize(48.dp).padding(16.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        }
    )
}*/

/*
@Composable
fun PasswordTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    hint: String = "" // Añadimos hint para que el usuario sepa qué escribir
) {
    var showPassword by remember { mutableStateOf(false) }
    BasicSecureTextField(
        state = state,
        textObfuscationMode = if (showPassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        // Ajustamos el estilo a tu nueva estética oscura
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
            .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp)) // Quitamos el LightGray
            .padding(12.dp),
        decorator = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                ) {
                    // Si el texto está vacío, mostramos el hint
                    if (state.text.isEmpty()) {
                        Text(text = hint, color = Color.Gray)
                    }
                    innerTextField()
                }
                Icon(
                    imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "Toggle password visibility",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .requiredSize(48.dp)
                        .padding(12.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        }
    )
}*/

//thegoodone
/*
@Composable
fun PasswordTextField(
    value: String, // Cambiado de TextFieldState a String
    onValueChange: (String) -> Unit, // Nuevo callback
    modifier: Modifier = Modifier,
    hint: String = ""
) {
    var showPassword by remember { mutableStateOf(false) }

    // Usamos BasicTextField para poder personalizar el diseño como ya tenías
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
            .padding(12.dp),
        textStyle = TextStyle(color = Color.White),
        cursorBrush = SolidColor(Color.White),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(text = hint, color = Color.Gray)
                    }
                    innerTextField()
                }
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        }
    )
}

*/


/*
@Preview(showBackground = true, name = "PasswordTextField")
@Composable
fun PasswordTextFieldPreview(
    @PreviewParameter(PasswordTextFieldPreviewParameterProvider::class)
    state: TextFieldState
) {
    LoomTheme {
        Box(Modifier.padding(16.dp)) {
            PasswordTextField(
                state = state
            )
        }
    }
}*/

/*
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    focusRequester: FocusRequester = remember { FocusRequester() }, // Añadido
    onDoneAction: () -> Unit = {} // Añadido
) {
    var showPassword by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        // Configuración del teclado
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDoneAction() }
        ),
        modifier = modifier
            .focusRequester(focusRequester) // Vincular el FocusRequester
            .fillMaxWidth()
            .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
            .padding(12.dp),
        textStyle = TextStyle(color = Color.White),
        cursorBrush = SolidColor(Color.White),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, end = 48.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(text = hint, color = Color.Gray)
                    }
                    innerTextField()
                }
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        }
    )
}
*/

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    focusRequester: FocusRequester = remember { FocusRequester() },
    imeAction: ImeAction = ImeAction.Done, // Nuevo parámetro
    onAction: () -> Unit = {}, // Renombrado para ser genérico (Next o Done)
    error: String? = null
) {
    var showPassword by remember { mutableStateOf(false) }

    Column() {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = { onAction() }
            ),
            modifier = modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = if(error != null)
                        Color.Red
                    else
                        Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            textStyle = TextStyle(color = Color.White),
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp, end = 48.dp)
                    ) {
                        if (value.isEmpty()) {
                            Text(text = hint, color = Color.Gray)
                        }
                        innerTextField()
                    }
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(24.dp)
                            .clickable { showPassword = !showPassword }
                    )
                }
            }
        )
        if(error != null) {
            Text(
                text = error,
                color = Color(0xFFFF6B6B),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 4.dp
                )
            )
        }
    }

}