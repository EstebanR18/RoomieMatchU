package com.example.approomiematchu.ui.authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomiematchu.data.remote.dto.LoginRequest
import com.example.approomiematchu.data.remote.dto.RegisterRequest
import com.example.approomiematchu.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var fullName = mutableStateOf("")
    var username = mutableStateOf("")
    var phone = mutableStateOf("")
    var errorMessage = mutableStateOf<String?>(null)
    var isLoading = mutableStateOf(false)

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.login(
                LoginRequest(
                    correo = email.value,
                    password = password.value
                )
            )

            result.onSuccess {
                errorMessage.value = null
                onSuccess()
            }.onFailure {
                errorMessage.value = it.message
            }
            isLoading.value = false
        }
    }

    fun register(onSuccess: () -> Unit) {
        val fullNameValue = fullName.value.trim()
        val usernameValue = username.value.trim()
        val phoneValue = phone.value.trim()
        val emailValue = email.value.trim()
        val passwordValue = password.value.trim()


        val phoneRegex = Regex("^\\d{8,10}$") // Solo números y entre 8 y 10 dígitos

        // Expresión regular igual que en el backend
        val passwordRegex =
            Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

        when {
            fullNameValue.isEmpty() ->
                errorMessage.value = "El nombre es obligatorio"

            usernameValue.isEmpty() ->
                errorMessage.value = "El usuario es obligatorio"

            phoneValue.isEmpty() ->
                errorMessage.value = "El teléfono es obligatorio"

            !phoneRegex.matches(phoneValue) ->
                errorMessage.value = "El teléfono debe tener solo números (8 a 10 dígitos)"

            emailValue.isEmpty() ->
                errorMessage.value = "El correo es obligatorio"

            !android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches() ->
                errorMessage.value = "El correo no es válido"

            !passwordRegex.matches(passwordValue) ->
                errorMessage.value =
                    "La contraseña debe tener mínimo 8 caracteres, incluir mayúscula, minúscula, número y símbolo"

            else -> {
                viewModelScope.launch {
                    isLoading.value = true
                    val result = repository.register(
                        RegisterRequest(
                            nombreCompleto = fullNameValue,
                            correo = emailValue,
                            telefono = phoneValue,
                            usuario = usernameValue,
                            password = passwordValue
                        )
                    )
                    result.onSuccess {
                        errorMessage.value = null
                        onSuccess()
                    }.onFailure {
                        errorMessage.value = it.message
                    }
                    isLoading.value = false
                }
            }
        }
    }

}
