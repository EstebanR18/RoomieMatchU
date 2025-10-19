package com.example.approomiematchu.data.remote.dto

data class UploadResponse(
    val message: String,
    val url: String? = null,
    val urls: List<String>? = null
)
