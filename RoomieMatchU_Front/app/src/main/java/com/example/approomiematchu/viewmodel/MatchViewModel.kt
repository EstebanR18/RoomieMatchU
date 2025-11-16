package com.example.approomiematchu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomiematchu.data.remote.api.ApiService
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MatchViewModel(private val api: ApiService) : ViewModel() {

    private val _perfiles = MutableStateFlow<List<PerfilResponse>>(emptyList())
    val perfiles: StateFlow<List<PerfilResponse>> = _perfiles

    private val _index = MutableStateFlow(0)
    val index: StateFlow<Int> = _index

    val perfilActual: StateFlow<PerfilResponse?> =
        combine(_perfiles, _index) { lista, idx ->
            lista.getOrNull(idx)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun cargarPerfiles(userId: Long) {
        viewModelScope.launch {
            val result = api.obtenerSugerencias(userId)
            _perfiles.value = result
            _index.value = 0
        }
    }

    fun like() { siguiente() }
    fun rechazar() { siguiente() }

    fun siguiente() {
        val next = _index.value + 1
        if (next < _perfiles.value.size) _index.value = next
    }

    fun atras() {
        val prev = _index.value - 1
        if (prev >= 0) _index.value = prev
    }
}
