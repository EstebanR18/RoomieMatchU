package com.example.approomiematchu.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("mensaje") val mensaje: String? = null,
    val url: String? = null,
    val urls: List<String>? = null
)
