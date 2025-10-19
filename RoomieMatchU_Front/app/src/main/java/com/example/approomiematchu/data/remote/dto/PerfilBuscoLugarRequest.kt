package com.example.approomiematchu.data.remote.dto

data class PerfilBuscoLugarRequest(
    // Obligatorios
    val fotoPerfil: String?, // puede venir nulo hasta que se suba
    val fechaNacimiento: String,
    val presupuesto: Double,
    val barrio: String,
    val habitos: String,

    // Nuevos campos
    val tipoHabitacion: String?,
    val tiempoEstancia: String?,
    val personasConvivencia: String?,
    val fechaMudanza: String?,
    val serviciosDeseados: String?,

    // Opcionales
    val genero: String?,
    val fuma: Boolean?,
    val alergico: Boolean?,
    val detalleAlergia: String?,
    val idioma: String?,
    val telefono: String?,
    val descripcionLibre: String?,
    val mascota: Boolean?
)