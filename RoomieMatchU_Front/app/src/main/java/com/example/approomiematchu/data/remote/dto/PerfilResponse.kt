package com.example.approomiematchu.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PerfilResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("usuario") val usuario: String?,
    @SerializedName("tipo") val tipo: String?, // "BUSCO_LUGAR" o "TENGO_LUGAR"
    @SerializedName("fotoPerfil") val fotoPerfil: String?,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String?,
    @SerializedName("barrio") val barrio: String?,
    @SerializedName("habitos") val habitos: String?,
    @SerializedName("genero") val genero: String?,
    @SerializedName("fuma") val fuma: Boolean?,
    @SerializedName("alergico") val alergico: Boolean?,
    @SerializedName("detalleAlergia") val detalleAlergia: String?,
    @SerializedName("idioma") val idioma: String?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("descripcionLibre") val descripcionLibre: String?,
    @SerializedName("mascota") val mascota: Boolean?,

    // Campos específicos de BUSCO_LUGAR
    @SerializedName("presupuesto") val presupuesto: Double?,
    @SerializedName("tipoHabitacion") val tipoHabitacion: String?,
    @SerializedName("tiempoEstancia") val tiempoEstancia: String?,
    @SerializedName("personasConvivencia") val personasConvivencia: String?,
    @SerializedName("fechaMudanza") val fechaMudanza: String?,
    @SerializedName("serviciosDeseados") val serviciosDeseados: String?,

    // Campos específicos de TENGO_LUGAR
    @SerializedName("arriendo") val arriendo: Double?,
    @SerializedName("cantidadHabitaciones") val cantidadHabitaciones: Int?,
    @SerializedName("maxRoomies") val maxRoomies: Int?,
    @SerializedName("reglasConvivencia") val reglasConvivencia: String?,
    @SerializedName("serviciosIncluidos") val serviciosIncluidos: String?,

    // Fotos
    @SerializedName("fotosResidenciaUrls") val fotosResidenciaUrls: List<String>?
)