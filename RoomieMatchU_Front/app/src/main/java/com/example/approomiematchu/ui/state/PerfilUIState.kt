package com.example.approomiematchu.ui.state

import com.example.approomiematchu.data.remote.dto.PerfilResponse

sealed class PerfilUIState {
    data object Loading : PerfilUIState()
    data class Data(val perfil: PerfilResponse) : PerfilUIState()
    data object Empty : PerfilUIState()
}