package com.loom.feature.auth.impl

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loom.core.data.repository.AuthRepository
import com.loom.core.model.data.VerifyEmailResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.compareTo

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val email = savedStateHandle.getStateFlow(key = EMAIL, initialValue = "")
    val username = savedStateHandle.getStateFlow(key = USERNAME, initialValue = "")
    val birthdate = savedStateHandle.getStateFlow(key = BIRTHDATE, initialValue = "")
    val password = savedStateHandle.getStateFlow(key = PASSWORD, initialValue = "")
    val confirmPassword = savedStateHandle.getStateFlow(key = CONFIRM_PASSWORD, initialValue = "")


    private val _registerState =
        MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)

    val registerState = _registerState.asStateFlow()


    private val _usernameValidationState =
        MutableStateFlow(UsernameValidationState())

    val usernameValidationState =
        _usernameValidationState.asStateFlow()

    fun onUsernameChanged(username: String) {
        savedStateHandle[USERNAME] = username
    }
    fun validateUsername(): Boolean {

        val currentUsername = username.value

        var error: String? = null

        when {

            currentUsername.isBlank() -> {
                error = "El nombre de usuario es obligatorio"
            }

            currentUsername.contains(" ") -> {
                error = "No puede contener espacios"
            }

            currentUsername.length < 3 -> {
                error = "Debe tener al menos 3 caracteres"
            }
        }

        _usernameValidationState.value =
            UsernameValidationState(error)

        return error == null
    }

    fun onRegisterTriggered() {

        _registerState.value = RegisterUiState.Loading

        viewModelScope.launch {

            val result = authRepository.register(
                username = username.value,
                email = email.value,
                password1 = password.value,
                password2 = confirmPassword.value,
                birthDate = birthdate.value,
            )

            result
                .onSuccess {
                    _registerState.value =
                        RegisterUiState.Success
                }
                .onFailure { throwable ->

                    val message =
                        throwable.message ?: "Error desconocido"

                    _usernameValidationState.value =
                        UsernameValidationState(
                            error = message
                        )

                    _registerState.value =
                        RegisterUiState.Error(message)
                }
        }
    }

    fun resetRegisterState() {
        _registerState.value = RegisterUiState.Idle
    }

    val birthMonth = savedStateHandle.getStateFlow(
        key = BIRTH_MONTH,
        initialValue = ""
    )

    val birthDay = savedStateHandle.getStateFlow(
        key = BIRTH_DAY,
        initialValue = ""
    )

    val birthYear = savedStateHandle.getStateFlow(
        key = BIRTH_YEAR,
        initialValue = ""
    )
    fun onBirthMonthChanged(month: String) {

        savedStateHandle[BIRTH_MONTH] = month

        updateBirthdate()
    }

    fun onBirthDayChanged(day: String) {

        savedStateHandle[BIRTH_DAY] = day

        updateBirthdate()
    }

    fun onBirthYearChanged(year: String) {

        savedStateHandle[BIRTH_YEAR] = year

        updateBirthdate()
    }

    private fun updateBirthdate() {

        val currentMonth = birthMonth.value
        val currentDay = birthDay.value
        val currentYear = birthYear.value

        if (
            currentMonth.isNotBlank() &&
            currentDay.isNotBlank() &&
            currentYear.isNotBlank()
        ) {

            val formattedDate =
                "$currentYear-$currentMonth-$currentDay"

            savedStateHandle[BIRTHDATE] = formattedDate
        }
    }

    fun onEmailChanged(email: String) {
        savedStateHandle[EMAIL] = email
    }

    fun onPasswordChanged(password: String) {
        savedStateHandle[PASSWORD] = password
    }




    private val _passwordValidationState =
        MutableStateFlow(PasswordValidationState())

    val passwordValidationState =
        _passwordValidationState.asStateFlow()


    fun validatePasswords(): Boolean {

        val passwordValue = password.value
        val confirmValue = confirmPassword.value

        var passwordError: String? = null
        var confirmError: String? = null

        when {
            passwordValue.length < 8 -> {
                passwordError = "Debe tener al menos 8 caracteres"
            }

            /*passwordValue.lowercase() in commonPasswords -> {
                passwordError = "Esta contraseña es demasiado común"
            }*/
        }

        if(passwordValue != confirmValue) {
            confirmError = "Las contraseñas no coinciden"
        }

        _passwordValidationState.value =
            PasswordValidationState(
                passwordError = passwordError,
                confirmPasswordError = confirmError
            )

        return passwordError == null && confirmError == null
    }


    private val _emailCheckState =
        MutableStateFlow<EmailCheckUiState>(EmailCheckUiState.Idle)
    val emailCheckState = _emailCheckState.asStateFlow()

    fun resetEmailCheckState() {
        _emailCheckState.value = EmailCheckUiState.Idle
    }

    // Esta variable sobrevive al reset del estado de carga
    var verifiedUserType by mutableStateOf<VerifyEmailResult?>(null)
        private set

    fun onEmailCheckTriggered() {

        val currentEmail = email.value

        if(currentEmail.isBlank()) return

        viewModelScope.launch {

            _emailCheckState.value = EmailCheckUiState.Loading

            val result = authRepository.verifyEmail(currentEmail)

            verifiedUserType = result // Guardamos el dato útil

            when(result) {

                VerifyEmailResult.ExistingUser -> {
                    _emailCheckState.value =
                        EmailCheckUiState.ExistingUser
                }

                VerifyEmailResult.NewUser -> {
                    _emailCheckState.value =
                        EmailCheckUiState.NewUser
                }

                is VerifyEmailResult.Error -> {
                    _emailCheckState.value =
                        EmailCheckUiState.Error
                }
            }
        }
    }
// ********************* Login ***************************
    private val _loginState =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val loginState = _loginState.asStateFlow()

    fun onLoginTriggered(){
        _loginState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = authRepository.login(
                email = email.value,
                password = password.value
            )

            if(result.isSuccess) {
                _loginState.value = LoginUiState.Success
            } else {
                _loginState.value = LoginUiState.Error
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginUiState.Idle
    }

    fun onConfirmPasswordChanged(password: String) {
        savedStateHandle[CONFIRM_PASSWORD] = password
    }

    val isRegisterButtonEnabled = combine(password, confirmPassword) { p1, p2 ->
        p1.isNotBlank() && p1 == p2 && p1.length >= 6 // Ejemplo de validación
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

}




private const val EMAIL = "email"
private const val USERNAME = "username"
private const val BIRTHDATE = "birthdate"
private const val PASSWORD = "password"
private const val CONFIRM_PASSWORD = "confirm_password"
private const val BIRTH_MONTH = "birth_month"
private const val BIRTH_DAY = "birth_day"
private const val BIRTH_YEAR = "birth_year"
