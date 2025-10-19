package com.example.approomiematchu.ui.authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomiematchu.data.repository.AuthRepository
import kotlinx.coroutines.launch

class PasswordResetViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    val email = mutableStateOf("")
    val token = mutableStateOf("")
    val newPassword = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val errorMessage = mutableStateOf<String?>(null)
    val successMessage = mutableStateOf<String?>(null)
    val isLoading = mutableStateOf(false)

    // Paso 1: Solicitar código
    fun requestCode(onSuccess: () -> Unit) {
        val correoValue = email.value.trim()

        if (correoValue.isEmpty()) {
            errorMessage.value = "El correo es obligatorio"
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            val result = repository.requestCode(correoValue)
            result.onSuccess {
                successMessage.value = "Código enviado"
                onSuccess()   // Ir a pantalla de código
            }.onFailure {
                errorMessage.value = it.message
            }
            isLoading.value = false
        }
    }

    // Paso 2: Validar token
    fun validateToken(onSuccess: () -> Unit) {
        val correoValue = email.value.trim()
        val tokenValue = token.value.trim()

        if (tokenValue.isEmpty()) {
            errorMessage.value = "Debe ingresar el código"
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            val result = repository.validateToken(correoValue, tokenValue)
            result.onSuccess {
                successMessage.value = "Código validado"
                onSuccess() // Ir a pantalla de nueva contraseña
            }.onFailure {
                errorMessage.value = it.message
            }
            isLoading.value = false
        }
    }

    // Paso 3: Restablecer contraseña
    fun resetPassword(onSuccess: () -> Unit) {
        val correoValue = email.value.trim()
        val tokenValue = token.value.trim()
        val passValue = newPassword.value.trim()
        val confirmValue = confirmPassword.value.trim()

        // Validaciones
        when {
            passValue.isEmpty() -> {
                errorMessage.value = "La contraseña es obligatoria"
                return
            }

            passValue != confirmValue -> {
                errorMessage.value = "Las contraseñas no coinciden"
                return
            }

            !passValue.matches(Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) -> {
                errorMessage.value = "Debe tener 8 caracteres, mayúscula, minúscula, número y símbolo"
                return
            }

            else -> {
                isLoading.value = true
                viewModelScope.launch {
                    val result = repository.resetPassword(correoValue, tokenValue, passValue)
                    result.onSuccess {
                        successMessage.value = "Contraseña actualizada"
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

