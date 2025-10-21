package com.example.approomiematchu.data.remote.dto

data class UserResponse(
    val id: Long,
    val nombreCompleto: String,
    val correo: String,
    val telefono: String,
    val usuario: String
)
