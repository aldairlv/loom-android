package com.loom.feature.auth.impl

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.ui.AuthButton
import com.loom.core.ui.NextActionButton
import com.loom.core.ui.PasswordTextField
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.loom.core.designsystem.theme.LoomTheme
import com.loom.core.ui.EmailTextField

@Composable
fun PasswordInputScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    email: String,
    isExistingUser: Boolean,
    onBackClick: () -> Unit,
    onNextClickRegister: (email: String, password: String, confirmPassword: String) -> Unit,
    onClickLogin: () -> Unit
){
    LaunchedEffect(email) {
        authViewModel.onEmailChanged(email)
    }

    val loginUiState by authViewModel.loginState.collectAsStateWithLifecycle()
    val password by authViewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by authViewModel.confirmPassword.collectAsStateWithLifecycle()
    val passwordValidationState by authViewModel.passwordValidationState.collectAsStateWithLifecycle()

    PasswordInputScreen(
        modifier = modifier,
        email = email,
        password = password,
        loginUiState = loginUiState,
        onLoginTriggered = authViewModel::onLoginTriggered,
        onPasswordChanged = authViewModel::onPasswordChanged,
        resetLoginState = authViewModel::resetLoginState,
        isExistingUser = isExistingUser,
        onBackClick = onBackClick,
        onNextClickRegister = onNextClickRegister,
        onNextClickLogin = onClickLogin,
        confirmPassword = confirmPassword,
        onConfirmPasswordChanged = authViewModel::onConfirmPasswordChanged,
        validatePasswords = authViewModel::validatePasswords,
        passwordValidationState = passwordValidationState


    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasswordInputScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    password: String = "",
    loginUiState: LoginUiState = LoginUiState.Idle,
    onLoginTriggered: () -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    isExistingUser: Boolean = false,
    onBackClick: () -> Unit,
    resetLoginState: () -> Unit = {},
    onNextClickRegister: (email: String, password: String, confirmPassword: String) -> Unit,
    onNextClickLogin: () -> Unit = {},
    confirmPassword: String = "",
    onConfirmPasswordChanged: (String) -> Unit = {},
    validatePasswords: () -> Boolean,
    passwordValidationState: PasswordValidationState
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF020817))
    ) {
        Box(modifier = Modifier
            .fillMaxSize()

        ){
            Column() {
                CenterAlignedTopAppBar(
                    title = { Text("room", color = Color.White) }, // Según tu imagen dice "room"
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
                    Log.d("PasswordInputScreen", "isExistingUser: $isExistingUser")
                    when {
                        isExistingUser -> LoginPasswordContent(
                            password = password,
                            onNextClickLogin = onNextClickLogin,
                            onLoginTriggered = onLoginTriggered,
                            onPasswordChanged = onPasswordChanged,
                            resetLoginState = resetLoginState,
                            loginUiState = loginUiState,
                            //focusRequester = focusRequester // <--- PASAR AQUÍ

                        )
                        else -> RegisterPasswordContent(
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword,
                            onPasswordChanged = onPasswordChanged,
                            onConfirmPasswordChanged = onConfirmPasswordChanged,
                            onNextClickRegister = onNextClickRegister,
                            validatePasswords =  validatePasswords,
                            passwordValidationState = passwordValidationState
                        )
                    }

                }
            }


        }
    }
}


@Composable
fun LoginPasswordContent(
    password: String = "",
    onNextClickLogin: () -> Unit,
    onLoginTriggered: () -> Unit,
    onPasswordChanged: (String) -> Unit = {},
    resetLoginState: () -> Unit = {},
    loginUiState: LoginUiState = LoginUiState.Idle,
) {
    val passwordState = remember { TextFieldState() }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(loginUiState) {
        when(loginUiState) {
            LoginUiState.Success -> {
                onNextClickLogin()
                resetLoginState()
            }
            else -> Unit
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Disfruta de tu rinconcito personal en internet!",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // USANDO TU COMPONENTE
        PasswordTextField(
            value = password,
            onValueChange = onPasswordChanged,
            hint = "Contraseña",
            focusRequester = focusRequester,
            imeAction = ImeAction.Done,
            onAction = onLoginTriggered
        )

        Text(
            text = "¿Has olvidado tu contraseña?",
            color = Color(0xFF0077B6),
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { /* Acción */ }
        )

        Spacer(modifier = Modifier.weight(1f))

        NextActionButton(
            text = "Iniciar sesión",
            onClick = onLoginTriggered,
            //containerColor = Color(0xFF0077B6),
            //contentColor = Color.White
        )
    }
}

@Composable
fun RegisterPasswordContent(
    email: String = "",
    password: String,
    confirmPassword: String,
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onNextClickRegister: (email:String, password: String, confirmPassword: String) -> Unit,
    validatePasswords: () -> Boolean,
    passwordValidationState: PasswordValidationState
) {
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester1.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Disfruta de tu rinconcito personal en internet! Nos encanta tenerte por aquí.",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        PasswordTextField(
            value = password,
            onValueChange = onPasswordChanged,
            hint = "Elige una contraseña",
            focusRequester = focusRequester1,
            imeAction = ImeAction.Next,
            onAction = { focusRequester2.requestFocus() },
            error = passwordValidationState.passwordError
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            hint = "Vuelve a escribirla",
            focusRequester = focusRequester2,
            imeAction = ImeAction.Done,
            error = passwordValidationState.confirmPasswordError,
            onAction = {
                if (validatePasswords()) {
                    onNextClickRegister(
                        email,
                        password,
                        confirmPassword
                    )
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        NextActionButton(
            text = "Siguiente",
            onClick = {
                if (validatePasswords()) {
                    onNextClickRegister(
                        email,
                        password,
                        confirmPassword
                    )
                }
            }
            //containerColor = Color(0xFF0077B6),
            //contentColor = Color.White
        )
    }
}






























/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasswordInputScreen(
    modifier: Modifier = Modifier,
    email: String = "",
    isExistingUser: Boolean = false,
    onBackClick: () -> Unit,
    onNextClickRegister: () -> Unit = {},
    onNextClickLogin: () -> Unit = {}
    // Aquí recibiriamos las acciones del ViewModel
) {
    Scaffold(
        containerColor = Color(0xFF020817), // Fondo oscuro de tu captura
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("room", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(LoomIcons.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Log.d("PasswordInputScreen", "isExistingUser: $isExistingUser")
            when {
                isExistingUser -> LoginPasswordContent(onNextClickLogin = onNextClickLogin)
                else -> RegisterPasswordContent(onNextClickRegister = onNextClickRegister)
            }
        }
    }
}

@Composable
fun LoginPasswordContent(
    onNextClickLogin: () -> Unit,
    //onLoginTriggered: () -> Unit
) {
    val passwordState = remember { TextFieldState() }


    //LaunchedEffect(authCheckState) {
    //  when(authCheckState) {
    //      AuthCheckUiState.Success -> {
    //          onNextClickLogin()
    //      }
    //    else -> Unit
    //  }
    //}


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Disfruta de tu rinconcito personal en internet!",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // USANDO TU COMPONENTE
        PasswordTextField(
            state = passwordState,
            hint = "Contraseña"
        )

        Text(
            text = "¿Has olvidado tu contraseña?",
            color = Color(0xFF0077B6),
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { /* Acción */ }
        )

        Spacer(modifier = Modifier.weight(1f))

        NextActionButton( // El botón que ya tenías definido
            text = "Iniciar sesión",
            onClick = {}//onLoginTriggered,
            //containerColor = Color(0xFF0077B6),
            //contentColor = Color.White
        )
    }
}

@Composable
fun RegisterPasswordContent(
    onNextClickRegister: () -> Unit
) {
    val pass1State = remember { TextFieldState() }
    val pass2State = remember { TextFieldState() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Disfruta de tu rinconcito personal en internet! Nos encanta tenerte por aquí.",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        PasswordTextField(
            state = pass1State,
            hint = "Elige una contraseña"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            state = pass2State,
            hint = "Vuelve a escribirla"
        )

        Spacer(modifier = Modifier.weight(1f))

        NextActionButton(
            text = "Siguiente",
            onClick = onNextClickRegister,
            //containerColor = Color(0xFF0077B6),
            //contentColor = Color.White
        )
    }
}




// Provider para alternar entre estados de usuario nuevo y existente
class PasswordScreenPreviewProvider : PreviewParameterProvider<EmailCheckUiState> {
    override val values = sequenceOf(
        EmailCheckUiState.ExistingUser,
        EmailCheckUiState.NewUser
    )
}

@Preview(showBackground = true, name = "Password Screen States")
@Composable
fun PasswordInputScreenPreview(
    @PreviewParameter(PasswordScreenPreviewProvider::class) state: EmailCheckUiState
) {
    LoomTheme {
        PasswordInputScreen(
            email = "ejemplo@correo.com",
            isExistingUser = state is EmailCheckUiState.ExistingUser,
            onBackClick = {},
            onNextClickRegister = {},
            onNextClickLogin = {}
        )
    }
}
*/