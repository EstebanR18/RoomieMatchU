package com.example.approomiematchu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomiematchu.data.remote.api.ApiService
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.ui.state.PerfilUIState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MatchViewModel(private val api: ApiService) : ViewModel() {

    private val _perfiles = MutableStateFlow<List<PerfilResponse>>(emptyList())
    private val _index = MutableStateFlow(0)

    // ESTADO UNIFICADO PARA ANIMAR
    val uiState: StateFlow<PerfilUIState> =
        combine(_perfiles, _index) { lista, idx ->
            when {
                lista.isEmpty() -> PerfilUIState.Empty
                idx < 0 || idx >= lista.size -> PerfilUIState.Empty
                else -> PerfilUIState.Data(lista[idx])
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, PerfilUIState.Loading)

    fun cargarPerfiles(userId: Long) {
        viewModelScope.launch {
            Log.d("MATCH", "Cargando perfiles para userId=$userId")

            val result = api.obtenerSugerencias(userId)

            Log.d("MATCH", "Perfiles recibidos=${result.size}")

            _perfiles.value = result

            _index.value = 0
        }
    }

    fun like() = siguiente()
    fun rechazar() = siguiente()

    fun siguiente() {
        val next = _index.value + 1
        val size = _perfiles.value.size

        Log.d("MATCH", "next=$next size=$size")

        if (next < size) {
            _index.value = next
            Log.d("MATCH", "Mostrando perfil idx=$next id=${_perfiles.value[next].id}")
        } else {
            Log.d("MATCH", "No hay más perfiles → estado EMPTY")
            _index.value = size // fuerza estado vacío
        }
    }

    fun atras() {
        val prev = _index.value - 1
        if (prev >= 0) {
            _index.value = prev
        }
    }
}
