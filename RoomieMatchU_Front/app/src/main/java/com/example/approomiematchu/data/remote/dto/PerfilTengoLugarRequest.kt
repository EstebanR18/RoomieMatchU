package com.example.approomiematchu.data.remote.dto

data class PerfilTengoLugarRequest(
    // Obligatorios
    val fotoPerfil: String?, // se actualiza tras subirla
    val fechaNacimiento: String,
    val arriendo: Double,
    val cantidadHabitaciones: Int,
    val maxRoomies: Int,
    val barrio: String,
    val habitos: String,

    // Opcionales
    val genero: String?,
    val fuma: Boolean?,
    val alergico: Boolean?,
    val detalleAlergia: String?,
    val idioma: String?,
    val telefono: String?,
    val descripcionLibre: String?,
    val reglasConvivencia: String?,
    val serviciosIncluidos: String?,
    val mascota: Boolean?
)