package com.example.approomiematchu.ui.profileconfig.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.approomiematchu.data.remote.api.ApiService
import com.example.approomiematchu.data.remote.dto.PerfilBuscoLugarRequest
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.data.remote.dto.PerfilTengoLugarRequest
import com.example.approomiematchu.utils.uriToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


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

    fun actualizarFotoPerfilLocal(uri: String) {
        _state.value = _state.value.copy(fotoPerfilLocalUri = uri)
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
    fun actualizarPresupuesto(presupuesto: Double?) {
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
    fun actualizarArriendo(arriendo: Double?) {
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
                    val bodyError = resp.errorBody()?.string()
                    onError("Código: ${resp.code()} - ${resp.message()}\n${bodyError ?: "Sin detalles"}")
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

                Log.d("PerfilEnvio", "📤 Enviando cuerpo TENGO_LUGAR:\n${body}")

                val resp = api.crearPerfilTengoLugar(_state.value.userId, body)
                val errorBody = resp.errorBody()?.string()
                Log.d("PerfilEnvio", "📥 Respuesta: code=${resp.code()} body=${resp.body()} error=$errorBody")

                if (resp.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Código: ${resp.code()} - ${resp.message()} \n${errorBody ?: "Sin detalles"}")
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error")
            }
        }
    }

    // -------------------------
    // SUBIDA DE FOTOS
    // -------------------------

    fun subirFotoPerfilAlFinal(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                val localUri = _state.value.fotoPerfilLocalUri
                if (localUri != null) {
                    val file = uriToFile(context, localUri)
                    if (file != null && file.exists()) {
                        val mimeType = context.contentResolver.getType(Uri.parse(localUri)) ?: "image/jpeg"

                        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData(
                            name = "file",
                            filename = file.name,
                            body = requestFile
                        )

                        val response = api.subirFotoPerfil(_state.value.userId, body)

                        if (response.isSuccessful) {
                            val uploadResponse = response.body()
                            val url = uploadResponse?.url ?: uploadResponse?.mensaje ?: ""
                            Log.d("PerfilEnvio", "📸 URL recibida del backend: $url")

                            if (url.isNotEmpty()) {
                                _state.value = _state.value.copy(
                                    fotoPerfilUrl = url,
                                    isLoading = false
                                )
                                onSuccess(url)
                            } else {
                                onError("El backend no devolvió una URL válida")
                            }
                        } else {
                            onError("Error al subir foto: ${response.errorBody()?.string() ?: response.message()}")
                        }
                    } else {
                        onError("Archivo de imagen no encontrado")
                    }
                } else {
                    onSuccess("") // No hay foto, continuar
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error inesperado")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }


    fun subirFotosResidencia(files: List<java.io.File>, onSuccess: (List<String>) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                val parts = files.map { file ->
                    val body = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData(
                        name = "files", // 👈 debe coincidir con @RestForm("files")
                        filename = file.name,
                        body = body
                    )
                }

                val response = api.subirFotosResidencia(_state.value.userId, parts)

                if (response.isSuccessful) {
                    val urls = response.body() ?: emptyList()
                    val nuevasFotos = _state.value.fotosResidencia + urls
                    _state.value = _state.value.copy(fotosResidencia = nuevasFotos)
                    onSuccess(urls)
                } else {
                    onError("Error al subir fotos: ${response.message()}")
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error inesperado")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun agregarFotoResidenciaLocal(uri: String) {
        val lista = _state.value.fotosResidencia.toMutableList()
        if (!lista.contains(uri)) { // evita duplicados
            lista.add(uri)
            _state.value = _state.value.copy(fotosResidencia = lista)
        }
    }

    fun actualizarFotosResidencia(fotos: List<String>) {
        _state.value = _state.value.copy(fotosResidencia = fotos)
    }

    fun editarPerfilBusco(onSuccess: () -> Unit, onError: (String) -> Unit) {
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

                val resp = api.editarPerfilBuscoLugar(_state.value.userId, body)
                if (resp.isSuccessful) {
                    onSuccess()
                } else {
                    val bodyError = resp.errorBody()?.string()
                    onError("Código: ${resp.code()} - ${resp.message()}\n${bodyError ?: "Sin detalles"}")
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error al editar perfil")
            }
        }
    }

    fun editarPerfilTengo(
        userProfile: PerfilResponse?, // nuevo parámetro
        context: Context,
        fechaNacimiento: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("PerfilEditar", "🧩 Iniciando actualización del perfil TENGO_LUGAR...")

        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                // ✅ 1️⃣ Mantener foto de perfil actual si no se cambió
                val fotoFinal = if (!_state.value.fotoPerfilUrl.isNullOrEmpty()) {
                    _state.value.fotoPerfilUrl
                } else {
                    userProfile?.fotoPerfil
                }

                // ✅ 2️⃣ Subir nuevas fotos de residencia si hay locales pendientes
                val fotosLocales = _state.value.fotosResidencia.filter { it.startsWith("content://") }
                var fotosFinales = _state.value.fotosResidencia.filterNot { it.startsWith("content://") }.toMutableList()

                if (fotosLocales.isNotEmpty()) {
                    val archivos = fotosLocales.mapNotNull { uriStr ->
                        uriToFile(context, uriStr)
                    }
                    Log.d("PerfilEditar", "📤 Subiendo ${archivos.size} nuevas fotos de residencia...")
                    val resp = api.subirFotosResidencia(_state.value.userId, archivos.map { file ->
                        val body = file.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("files", file.name, body)
                    })
                    if (resp.isSuccessful) {
                        val urlsSubidas = resp.body() ?: emptyList()
                        fotosFinales.addAll(urlsSubidas)
                        Log.d("PerfilEditar", "✅ Fotos subidas correctamente: $urlsSubidas")
                    } else {
                        Log.e("PerfilEditar", "⚠️ Error al subir fotos: ${resp.message()}")
                    }
                }

                // ✅ 3️⃣ Construir body del perfil con las fotos actualizadas
                val body = PerfilTengoLugarRequest(
                    fotoPerfil = fotoFinal,
                    fechaNacimiento = fechaNacimiento,
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

                // 🧾 Log de envío
                Log.d("PerfilEditar", "📦 Cuerpo enviado:\n$body")
                Log.d("PerfilEditar", "📸 Fotos residencia enviadas: $fotosFinales")

                // ✅ 4️⃣ Enviar actualización del perfil
                val response = api.editarPerfilTengoLugar(_state.value.userId, body)
                Log.d("PerfilEditar", "📥 Respuesta: code=${response.code()}, success=${response.isSuccessful}")

                if (response.isSuccessful) {
                    Log.d("PerfilEditar", "✅ Perfil actualizado correctamente en el backend")
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PerfilEditar", "❌ Error al actualizar: ${response.message()} - $errorBody")
                    onError("Error ${response.code()}: ${response.message()} \n$errorBody")
                }
            } catch (e: Exception) {
                Log.e("PerfilEditar", "❌ Excepción al actualizar perfil", e)
                onError(e.localizedMessage ?: "Error desconocido")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    fun eliminarFotosResidencia(
        urls: List<String>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userId = state.value.userId ?: run {
                    Log.e("PerfilVM", "❌ ID de usuario no encontrado")
                    onError("ID de usuario no encontrado")
                    return@launch
                }

                Log.d("PerfilVM", "🗑️ === INICIANDO ELIMINACIÓN DE FOTOS ===")
                Log.d("PerfilVM", "👤 UserId: $userId")
                Log.d("PerfilVM", "📸 URLs a eliminar: $urls")
                Log.d("PerfilVM", "🔢 Cantidad de URLs: ${urls.size}")

                if (urls.isEmpty()) {
                    Log.e("PerfilVM", "❌ Lista de URLs vacía")
                    onError("No hay URLs para eliminar")
                    return@launch
                }

                // 🔥 CAMBIO IMPORTANTE: Convertir List<String> a String separado por comas
                val urlsString = urls.joinToString(",")
                Log.d("PerfilVM", "📤 URLs como string: $urlsString")

                val response = api.eliminarFotosResidenciaEspecificas(userId, urlsString)

                Log.d("PerfilVM", "📡 Response code: ${response.code()}")
                Log.d("PerfilVM", "📡 Response isSuccessful: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    Log.d("PerfilVM", "✅ Fotos eliminadas correctamente en el backend")

                    // Actualizar estado local
                    val estadoActual = _state.value.fotosResidencia
                    val nuevasFotos = estadoActual.filterNot { it in urls }
                    _state.value = _state.value.copy(fotosResidencia = nuevasFotos)

                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PerfilVM", "❌ Error response: $errorBody")
                    Log.e("PerfilVM", "❌ Error code: ${response.code()}")

                    val errorMsg = errorBody ?: "Error desconocido (código: ${response.code()})"
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                Log.e("PerfilVM", "❌ Excepción al eliminar fotos: ${e.message}")
                onError(e.message ?: "Error inesperado")
            }
        }
    }
}