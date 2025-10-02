package com.example.approomiematchu.data.remote.dto

data class RegisterRequest(
    val nombreCompleto: String,
    val correo: String,
    val telefono: String,
    val usuario: String,
    val password: String
)