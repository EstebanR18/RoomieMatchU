package com.example.approomiematchu.data.remote.dto

data class PerfilResponse(
    val id: Long,
    val tipo: String,
    val fotoPerfil: String?,
    val fotosResidenciaUrls: List<String>?
)
