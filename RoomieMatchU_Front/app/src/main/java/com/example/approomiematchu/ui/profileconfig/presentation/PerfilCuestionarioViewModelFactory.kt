package com.example.approomiematchu.ui.profileconfig.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.approomiematchu.data.remote.api.ApiService

class PerfilCuestionarioViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilCuestionarioViewModel::class.java)) {
            return PerfilCuestionarioViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
