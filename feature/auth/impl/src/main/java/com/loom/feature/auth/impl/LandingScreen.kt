package com.loom.feature.auth.impl

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.designsystem.R.drawable
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.ui.AsyncImage
import com.loom.core.ui.AuthButton

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onContinueEmail: () -> Unit
) {
    LandingScreen(
        onContinueEmail = onContinueEmail
    )
}

@Composable
internal fun LandingScreen(
    onContinueEmail: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo (Imagen/Video)
        AsyncImage(imageUrl = "https://picsum.photos/200/300")

        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                /*
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                    endY = 100f
                )*/
                color = Color.Black.copy(alpha = 0.6f)
            )
        ){
            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Spacer(Modifier.weight(0.30f))
                Text(
                    text = "room",
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                )
                Spacer(Modifier.weight(0.15f))
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¡Disfruta de tu rinconcito personal en internet!",
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text (
                            text = "Jamás volverás a aburrirte.",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Regístrate o inicia sesión:",
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White,

                    )
                AuthButton(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Continua con Google",
                    icon = painterResource(
                        drawable.android_neutral_sq_na
                    ),
                    onClick = { /* Google */ }

                )
                AuthButton(
                    text = "Continua con tu correo",
                    icon = LoomIcons.Email,
                    onClick = onContinueEmail
                )

            }
        }

    }
}

@Preview(showBackground = true, name = "Landing")
@Composable
fun LandingScreenPreview(
    //@PreviewParameter(LandingPreviewParameterProvider::class)
    //LandingObjects: List<LandingObject>,
) {
    LoomTheme {
        LandingScreen(
            onContinueEmail = {}
        )
    }
}