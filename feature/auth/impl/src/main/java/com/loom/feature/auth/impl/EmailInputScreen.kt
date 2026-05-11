package com.loom.feature.auth.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.designsystem.theme.LoomTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.loom.core.designsystem.R.drawable
import com.loom.core.ui.AsyncImage
import com.loom.core.ui.AuthButton
import com.loom.core.ui.EmailTextField
import com.loom.core.ui.NextActionButton

@Composable
fun EmailInputScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: (email: String, isExistingUser: Boolean) -> Unit
) {
    val email by authViewModel.email.collectAsStateWithLifecycle()
    val emailCheckState by authViewModel.emailCheckState.collectAsStateWithLifecycle()

    EmailInputScreen(
        modifier = modifier,
        email = email,
        emailCheckState = emailCheckState,
        onEmailChanged = authViewModel::onEmailChanged,
        onEmailCheckTriggered = authViewModel::onEmailCheckTriggered,
        resetEmailCheckState = authViewModel::resetEmailCheckState,
        onBackClick = onBackClick,
        onNextClick = onNextClick
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailInputScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    emailCheckState: EmailCheckUiState = EmailCheckUiState.Idle,
    onEmailChanged: (String) -> Unit = {},
    onEmailCheckTriggered: () -> Unit = {},
    resetEmailCheckState: () -> Unit = {},
    onBackClick: () -> Unit,
    onNextClick: (email: String, isExistingUser: Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(emailCheckState) {
        when(emailCheckState) {

            EmailCheckUiState.ExistingUser -> {
                onNextClick(email, true)
                resetEmailCheckState()
            }

            EmailCheckUiState.NewUser -> {
                onNextClick(email, false)
                resetEmailCheckState()
            }

            else -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF020817))
    ) {
        Box(modifier = Modifier
            .fillMaxSize()

        ){
            Column() {
                CenterAlignedTopAppBar(
                    title = { Text("room", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = LoomIcons.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .imePadding()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {


                    Text(
                        text = "Introduce tu correo para registrarte o iniciar sesión:",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 32.dp, top = 16.dp)
                    )

                    EmailTextField(
                        value = email,
                        onValueChange = onEmailChanged,
                        placeholder = "Correo electrónico",
                        focusRequester = focusRequester,
                        onNextAction = onEmailCheckTriggered
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    NextActionButton(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = "Siguiente",
                        onClick = onEmailCheckTriggered,
                        enabled = email.isNotBlank()
                    )
                }
            }


        }
    }
}


@Preview(showBackground = true, name = "EmailInput")
@Composable
fun EmailInputScreenPreview() {
    LoomTheme {
        EmailInputScreen(
            email = "ejemplo@correo.com",
            emailCheckState = EmailCheckUiState.Idle,
            onEmailChanged = {},
            onEmailCheckTriggered = {},
            onBackClick = {},
            onNextClick = { _, _ -> }
        )
    }
}


