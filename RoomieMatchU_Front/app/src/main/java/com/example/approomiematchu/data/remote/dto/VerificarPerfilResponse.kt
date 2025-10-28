package com.example.approomiematchu.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VerificarPerfilResponse(
    @SerializedName("tienePerfil") val tienePerfil: Boolean,
    @SerializedName("tipoPerfil") val tipoPerfil: String? // "BUSCO_LUGAR", "TENGO_LUGAR", o null
)