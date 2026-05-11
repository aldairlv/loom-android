package com.loom.feature.auth.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.loom.core.designsystem.icon.LoomIcons
import com.loom.core.ui.NextActionButton
import com.loom.core.ui.UsernameTextField

@Composable
fun CompleteRegisterUsernameScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    email: String,
    password: String,
    confirmPassword: String,
    birthMonth: String,
    birthDay: String,
    birthYear: String
) {
    LaunchedEffect(email, password, confirmPassword, birthMonth,birthDay, birthYear) {
        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)
        viewModel.onConfirmPasswordChanged(confirmPassword)
        viewModel.onBirthMonthChanged(birthMonth)
        viewModel.onBirthDayChanged(birthDay)
        viewModel.onBirthYearChanged(birthYear)
    }

    val username by viewModel.username.collectAsStateWithLifecycle()
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()
    val usernameValidationState by viewModel.usernameValidationState.collectAsStateWithLifecycle()

    CompleteRegisterUsernameScreen(
        modifier = modifier,
        username = username,
        registerState = registerState,
        usernameValidationState = usernameValidationState,
        onUsernameChanged = viewModel::onUsernameChanged,
        onBackClick = onBackClick,
        onRegisterTriggered = viewModel::onRegisterTriggered,
        validateUsername = viewModel::validateUsername,
        resetRegisterState = viewModel::resetRegisterState,
        onRegisterSuccess = onRegisterSuccess
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CompleteRegisterUsernameScreen(
    modifier: Modifier = Modifier,
    username: String = "",
    registerState: RegisterUiState = RegisterUiState.Idle,
    usernameValidationState: UsernameValidationState =
        UsernameValidationState(),
    onUsernameChanged: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onRegisterTriggered: () -> Unit = {},
    validateUsername: () -> Boolean = { false },
    resetRegisterState: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {

    LaunchedEffect(registerState) {
        when(registerState) {
            RegisterUiState.Success -> {
                onRegisterSuccess()
                resetRegisterState()
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020817))
    ) {

        Column {

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "room",
                        color = Color.White
                    )
                },
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(64.dp))

                Text(
                    text = "¿Cómo te llamamos?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Este será el nombre con el que te verán otras personas en Room y también tu URL. No te preocupes: podrás cambiarlo cuando quieras.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                UsernameTextField(
                    value = username,
                    onValueChange = onUsernameChanged,
                    hint = "Nombre de usuario",
                    error = usernameValidationState.error,
                    onDone = {
                        if(validateUsername()) {
                            onRegisterTriggered()
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                NextActionButton(
                    text = "Regístrate",
                    onClick = {
                        if(validateUsername()) {
                            onRegisterTriggered()
                        }
                    }
                )
            }
        }
    }
}