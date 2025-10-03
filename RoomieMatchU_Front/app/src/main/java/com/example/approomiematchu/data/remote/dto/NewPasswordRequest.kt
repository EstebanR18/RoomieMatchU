package com.example.approomiematchu.data.remote.dto

data class NewPasswordRequest(
    val correo: String,
    val token: String,
    val newPassword: String
)