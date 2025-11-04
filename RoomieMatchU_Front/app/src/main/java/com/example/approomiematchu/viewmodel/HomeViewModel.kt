package com.example.approomiematchu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomiematchu.data.remote.RetrofitClient
import com.example.approomiematchu.data.remote.api.ApiService
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.data.remote.dto.UserResponse
import com.example.approomiematchu.data.repository.UserRepository
import com.example.approomiematchu.ui.profileform.presentation.TipoPerfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val apiService: ApiService) : ViewModel() {

    private val userRepository = UserRepository(RetrofitClient.instance)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _userProfile = MutableStateFlow<PerfilResponse?>(null)
    val userProfile: StateFlow<PerfilResponse?> = _userProfile.asStateFlow()

    // Agregar StateFlow para los datos del usuario
    private val _userData = MutableStateFlow<UserResponse?>(null)
    val userData: StateFlow<UserResponse?> = _userData.asStateFlow()

    fun loadUserProfile(userId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Obtener datos del usuario (nombre real, email, etc.)
                val userDataResult = userRepository.getUserById(userId)
                userDataResult.onSuccess { userData ->
                    _userData.value = userData
                }.onFailure { error ->
                    // No detenemos el flujo si falla obtener datos del usuario, solo log
                    Log.e("HomeViewModel", "Error obteniendo datos del usuario: ${error.message}")
                }

                // Obtener perfil del usuario
                val perfilResult = userRepository.obtenerPerfilUsuario(userId)
                perfilResult.onSuccess { perfil ->
                    _userProfile.value = perfil
                    val tipoPerfil = when (perfil.tipo) {
                        "BUSCO_LUGAR" -> TipoPerfil.BUSCO_LUGAR
                        "TENGO_LUGAR" -> TipoPerfil.TENGO_LUGAR
                        else -> TipoPerfil.NONE
                    }
                    _uiState.value = _uiState.value.copy(
                        tipoPerfil = tipoPerfil,
                        isLoading = false,
                        error = null
                    )
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar perfil"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error inesperado"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class HomeUiState(
    val tipoPerfil: TipoPerfil = TipoPerfil.NONE,
    val isLoading: Boolean = false,
    val error: String? = null
)