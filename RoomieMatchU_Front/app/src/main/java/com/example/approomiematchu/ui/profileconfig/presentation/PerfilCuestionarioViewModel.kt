package com.example.approomiematchu.ui.profileconfig.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomiematchu.data.remote.api.ApiService
import com.example.approomiematchu.data.remote.dto.PerfilBuscoLugarRequest
import com.example.approomiematchu.data.remote.dto.PerfilTengoLugarRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerfilCuestionarioViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _state = MutableStateFlow(PerfilState())
    val state: StateFlow<PerfilState> = _state

    // -------------------------
    // SETTERS BÁSICOS
    // -------------------------
    fun setUserId(id: Long) {
        _state.value = _state.value.copy(userId = id)
    }

    fun setTipoPerfil(tipo: TipoPerfil) {
        _state.value = _state.value.copy(tipoPerfil = tipo)
    }

    fun actualizarCampoFechaNacimiento(fecha: String) {
        _state.value = _state.value.copy(fechaNacimiento = fecha)
    }

    fun actualizarFotoPerfil(url: String) {
        _state.value = _state.value.copy(fotoPerfilUrl = url)
    }

    fun avanzarPaso() {
        val paso = _state.value.pasoActual + 1
        _state.value = _state.value.copy(pasoActual = paso)
    }

    fun retrocederPaso() {
        val paso = _state.value.pasoActual - 1
        if (paso >= 1) {
            _state.value = _state.value.copy(pasoActual = paso)
        }
    }

    // -------------------------
    // CAMPOS COMUNES
    // -------------------------
    fun actualizarGenero(genero: String) {
        _state.value = _state.value.copy(genero = genero)
    }

    fun actualizarTelefono(telefono: String) {
        _state.value = _state.value.copy(telefono = telefono)
    }

    fun actualizarIdioma(idioma: String) {
        _state.value = _state.value.copy(idioma = idioma)
    }

    fun actualizarHabitos(habitos: String) {
        _state.value = _state.value.copy(habitos = habitos)
    }

    fun actualizarMascota(tiene: Boolean) {
        _state.value = _state.value.copy(mascota = tiene)
    }

    fun actualizarFuma(fuma: Boolean) {
        _state.value = _state.value.copy(fuma = fuma)
    }

    fun actualizarAlergico(esAlergico: Boolean, detalle: String?) {
        _state.value = _state.value.copy(
            alergico = esAlergico,
            detalleAlergia = detalle
        )
    }

    fun actualizarDescripcionLibre(descripcion: String) {
        _state.value = _state.value.copy(descripcionLibre = descripcion)
    }

    // -------------------------
    // CAMPOS BUSCO_LUGAR
    // -------------------------
    fun actualizarPresupuesto(presupuesto: Double) {
        _state.value = _state.value.copy(presupuesto = presupuesto)
    }

    fun actualizarBarrio(barrio: String) {
        _state.value = _state.value.copy(barrio = barrio)
    }

    fun actualizarTipoHabitacion(tipo: String) {
        _state.value = _state.value.copy(tipoHabitacion = tipo)
    }

    fun actualizarTiempoEstancia(tiempo: String) {
        _state.value = _state.value.copy(tiempoEstancia = tiempo)
    }

    fun actualizarPersonasConvivencia(cantidad: String) {
        _state.value = _state.value.copy(personasConvivencia = cantidad)
    }

    fun actualizarFechaMudanza(fecha: String) {
        _state.value = _state.value.copy(fechaMudanza = fecha)
    }

    fun actualizarServiciosDeseados(servicios: String) {
        _state.value = _state.value.copy(serviciosDeseados = servicios)
    }

    // -------------------------
    // CAMPOS TENGO_LUGAR
    // -------------------------
    fun actualizarArriendo(arriendo: Double) {
        _state.value = _state.value.copy(arriendo = arriendo)
    }

    fun actualizarCantidadHabitaciones(cantidad: Int) {
        _state.value = _state.value.copy(cantidadHabitaciones = cantidad)
    }

    fun actualizarMaxRoomies(max: Int) {
        _state.value = _state.value.copy(maxRoomies = max)
    }

    fun actualizarReglasConvivencia(reglas: String) {
        _state.value = _state.value.copy(reglasConvivencia = reglas)
    }

    fun actualizarServiciosIncluidos(servicios: String) {
        _state.value = _state.value.copy(serviciosIncluidos = servicios)
    }

    // -------------------------
    // FOTOS DE RESIDENCIA
    // -------------------------
    fun agregarFotoResidencia(url: String) {
        val lista = _state.value.fotosResidencia.toMutableList()
        lista.add(url)
        _state.value = _state.value.copy(fotosResidencia = lista)
    }

    //------------------------------
    // ENVÍO DE PERFIL AL BACKEND
    //------------------------------
    fun enviarPerfilBusco(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val body = PerfilBuscoLugarRequest(
                    fotoPerfil = _state.value.fotoPerfilUrl,
                    fechaNacimiento = _state.value.fechaNacimiento,
                    presupuesto = _state.value.presupuesto ?: 0.0,
                    barrio = _state.value.barrio ?: "",
                    habitos = _state.value.habitos ?: "",
                    tipoHabitacion = _state.value.tipoHabitacion,
                    tiempoEstancia = _state.value.tiempoEstancia,
                    personasConvivencia = _state.value.personasConvivencia,
                    fechaMudanza = _state.value.fechaMudanza,
                    serviciosDeseados = _state.value.serviciosDeseados,
                    genero = _state.value.genero,
                    fuma = _state.value.fuma,
                    alergico = _state.value.alergico,
                    detalleAlergia = _state.value.detalleAlergia,
                    idioma = _state.value.idioma,
                    telefono = _state.value.telefono,
                    descripcionLibre = _state.value.descripcionLibre,
                    mascota = _state.value.mascota
                )

                val resp = api.crearPerfilBuscoLugar(_state.value.userId, body)
                if (resp.isSuccessful) {
                    onSuccess()
                } else {
                    onError(resp.message())
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error")
            }
        }
    }

    fun enviarPerfilTengo(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val body = PerfilTengoLugarRequest(
                    fotoPerfil = _state.value.fotoPerfilUrl,
                    fechaNacimiento = _state.value.fechaNacimiento,
                    arriendo = _state.value.arriendo ?: 0.0,
                    cantidadHabitaciones = _state.value.cantidadHabitaciones ?: 0,
                    maxRoomies = _state.value.maxRoomies ?: 1,
                    barrio = _state.value.barrio ?: "",
                    habitos = _state.value.habitos ?: "",
                    genero = _state.value.genero,
                    fuma = _state.value.fuma,
                    alergico = _state.value.alergico,
                    detalleAlergia = _state.value.detalleAlergia,
                    idioma = _state.value.idioma,
                    telefono = _state.value.telefono,
                    descripcionLibre = _state.value.descripcionLibre,
                    reglasConvivencia = _state.value.reglasConvivencia,
                    serviciosIncluidos = _state.value.serviciosIncluidos,
                    mascota = _state.value.mascota
                )

                val resp = api.crearPerfilTengoLugar(_state.value.userId, body)
                if (resp.isSuccessful) {
                    onSuccess()
                } else {
                    onError(resp.message())
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error")
            }
        }
    }
}