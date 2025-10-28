package com.example.approomiematchu.ui

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.approomiematchu.R
import com.example.approomiematchu.data.remote.RetrofitClient
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.data.remote.dto.UserResponse
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.ui.home.HomeViewModel
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModelFactory
import com.example.approomiematchu.utils.uriToFile
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun PerfilTengoLugarScreen(
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null,
    navController: NavController
) {
    PerfilVerScreenTengoLugar(
        userProfile = userProfile,
        userData = userData,
        navController = navController
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilVerScreenTengoLugar(
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null,
    navController: NavController
) {
    val scrollState = rememberScrollState()

    // Nombre siempre recalculado con userProfile actualizado
    val nombreConEdad = run {
        val nombreReal = userData?.nombreCompleto ?: "Mi perfil"
        val fechaNacimiento = userProfile?.fechaNacimiento
        if (!fechaNacimiento.isNullOrEmpty()) {
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val fechaNac = formato.parse(fechaNacimiento)
                val hoy = Calendar.getInstance()
                val nacimiento = Calendar.getInstance().apply { time = fechaNac }
                var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
                if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) edad--
                "$nombreReal, $edad"
            } catch (_: Exception) {
                nombreReal
            }
        } else nombreReal
    }

    val serviciosIncluidos = userProfile?.serviciosIncluidos
        ?.split(",")?.map { it.trim() }.orEmpty()

    val reglasConvivencia = userProfile?.reglasConvivencia
        ?.split(",")?.map { it.trim() }.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0))
            .padding(24.dp)
    ) {
        // 🔹 Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_atras_screens),
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(28.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Text(
                text = "MI PERFIL",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp)
                    .clickable {
                        navController.navigate(AppScreens.PerfilEditarTengoLugar.route)
                    }
            )
        }

        // 🔹 Contenido
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            // Foto de perfil
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                val fotoPerfil = userProfile?.fotoPerfil
                if (!fotoPerfil.isNullOrEmpty()) {
                    AsyncImage(
                        model = fotoPerfil,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.imagen2),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = nombreConEdad,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Fotos de la residencia
            userProfile?.fotosResidenciaUrls?.let { fotos ->
                if (fotos.isNotEmpty()) {
                    ResidenciaPhotosGrid(
                        photos = fotos,
                        onAddPhoto = { /* agregar nueva foto más adelante */ }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 🔸 Descripción
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción del lugar",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = userProfile?.descripcionLibre?.takeIf { it.isNotEmpty() }
                            ?: "Aún no tienes una descripción. ¡Agrega una para contar más sobre tu lugar!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (userProfile?.descripcionLibre.isNullOrEmpty()) Color.Gray else Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Zona / barrio
            Text(
                "Zona o barrio",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.barrio ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Precio
            Text(
                "Precio del arriendo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.arriendo?.let { "$${"%,.0f".format(it)}" }
                ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Habitaciones
            Text(
                "Habitaciones disponibles",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(
                listOfNotNull(
                    userProfile?.cantidadHabitaciones?.toString() ?: "No definido"
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Máximo de roomies
            Text(
                "Máximo de roomies",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.maxRoomies?.toString() ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Servicios incluidos
            Text(
                "Servicios incluidos en el arriendo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(serviciosIncluidos.ifEmpty { listOf("No especificado") })

            Spacer(modifier = Modifier.height(16.dp))

            // Reglas convivencia
            Text(
                "Reglas de convivencia",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(reglasConvivencia.ifEmpty { listOf("No especificado") })

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilEditarScreenTengoLugar(
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null,
    viewModel: PerfilCuestionarioViewModel = viewModel(
        factory = PerfilCuestionarioViewModelFactory(RetrofitClient.instance)
    ),
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    var isSaving by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Estados para manejar fotos
    val fotosResidencia = remember {
        mutableStateListOf<String>().apply {
            addAll(userProfile?.fotosResidenciaUrls ?: emptyList())
        }
    }

    // Lista para trackear fotos eliminadas
    val fotosEliminadas = remember { mutableStateListOf<String>() }

    // Estado para la nueva foto de perfil
    var nuevaFotoPerfilUri by remember { mutableStateOf<Uri?>(null) }
    var fotoPerfilActualizada by remember { mutableStateOf(false) }

    // Launcher para foto de perfil
    val launcherFotoPerfil = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            nuevaFotoPerfilUri = it
            fotoPerfilActualizada = true
            viewModel.actualizarFotoPerfilLocal(it.toString())
        }
    }

    // Launcher para fotos de residencia
    val launcherFotosResidencia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (fotosResidencia.size < 6) {
                fotosResidencia.add(it.toString())
                Log.d("Fotos", "✅ Foto agregada. Total: ${fotosResidencia.size}")
            } else {
                Toast.makeText(context, "Máximo 6 fotos permitidas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función mejorada para eliminar fotos
    val onDeletePhoto = { index: Int ->
        Log.d("Fotos", "🗑️ Intentando eliminar foto en índice: $index, Total fotos: ${fotosResidencia.size}")

        if (index in fotosResidencia.indices) {
            if (fotosResidencia.size > 1) {
                val fotoEliminada = fotosResidencia[index]
                Log.d("Fotos", "📸 Foto a eliminar: ${fotoEliminada.take(50)}...")

                // Si es una foto existente (URL), agregar a la lista de eliminadas
                if (fotoEliminada.startsWith("http")) {
                    fotosEliminadas.add(fotoEliminada)
                    Log.d("Fotos", "📝 Agregada a fotosEliminadas. Total: ${fotosEliminadas.size}")
                }

                // Eliminar de la lista visual
                fotosResidencia.removeAt(index)
                Log.d("Fotos", "✅ Foto eliminada. Nuevo total: ${fotosResidencia.size}")

                Toast.makeText(context, "Foto eliminada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Debe haber al menos una foto", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("Fotos", "❌ Índice fuera de rango: $index, tamaño: ${fotosResidencia.size}")
            Toast.makeText(context, "Error: índice no válido", Toast.LENGTH_SHORT).show()
        }
    }

    // Calcular edad
    fun calcularEdad(fechaNacimiento: String?): Int? {
        return try {
            if (fechaNacimiento == null) return null
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaNac = formato.parse(fechaNacimiento)
            val hoy = Calendar.getInstance()
            val nacimiento = Calendar.getInstance().apply { time = fechaNac }
            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
            if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) edad--
            edad
        } catch (e: Exception) {
            null
        }
    }

    // Estados editables
    var descripcion by remember { mutableStateOf(userProfile?.descripcionLibre ?: "") }
    var arriendo by remember { mutableStateOf(userProfile?.arriendo?.toString() ?: "") }
    var cantidadHabitaciones by remember {
        mutableStateOf(
            userProfile?.cantidadHabitaciones?.toString() ?: ""
        )
    }
    var maxRoomies by remember { mutableStateOf(userProfile?.maxRoomies?.toString() ?: "") }

    var barrio by remember { mutableStateOf(userProfile?.barrio ?: "") }
    var serviciosIncluidos by remember { mutableStateOf(userProfile?.serviciosIncluidos ?: "") }
    var reglasConvivencia by remember { mutableStateOf(userProfile?.reglasConvivencia ?: "") }

    // Nombre con edad
    val nombreConEdad = remember(userProfile, userData) {
        val nombre = userData?.nombreCompleto ?: "Usuario"
        userProfile?.fechaNacimiento?.let { fecha ->
            val edad = calcularEdad(fecha)
            if (edad != null) "$nombre, $edad" else nombre
        } ?: nombre
    }

    // Zonas disponibles
    val zonas = listOf(
        "Usaquén", "Chapinero", "Santa Fe", "San Cristóbal", "Usme",
        "Tunjuelito", "Bosa", "Kennedy", "Fontibón", "Engativá",
        "Suba", "Barrios Unidos", "Teusaquillo", "Los Mártires",
        "Antonio Nariño", "Puente Aranda", "La Candelaria",
        "Rafael Uribe Uribe", "Ciudad Bolívar", "Sumapaz"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0))
            .padding(24.dp)
    ) {
        // 🔹 Encabezado
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_atras_screens),
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(28.dp)
                    .clickable {
                        navController.navigate(AppScreens.PerfilTengoLugar.route)
                    }
            )

            Text(
                text = "EDITAR PERFIL",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 🔹 Contenido con scroll
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            // 📷 Foto de perfil
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val fotoActual = nuevaFotoPerfilUri?.toString() ?: userProfile?.fotoPerfil
                if (!fotoActual.isNullOrEmpty()) {
                    AsyncImage(
                        model = fotoActual,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.imagen2),
                        contentDescription = "Foto perfil",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Cambiar foto",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(36.dp)
                        .clickable { launcherFotoPerfil.launch("image/*") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre + edad
            Text(
                text = nombreConEdad,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grid editable
            ResidenciaPhotosGridEditar(
                photos = fotosResidencia,
                onAddPhoto = { launcherFotosResidencia.launch("image/*") },
                onDeletePhoto = onDeletePhoto
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Descripción
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción del lugar",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        placeholder = { Text("Aquí puedes agregar una descripción del lugar") },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = customTextFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Zona / barrio
            Text(
                "Zona o barrio",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                zonas.forEach { zona ->
                    val selected = zona == barrio
                    Chip(
                        text = zona,
                        isSelected = selected,
                        onClick = { barrio = zona }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* Precio del arriendo */
            Text(
                "Precio del arriendo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            TextField(
                value = arriendo,
                onValueChange = { arriendo = it },
                placeholder = { Text("Ejemplo: 1.400.000") },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = customTextFieldColors(
                    containerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* Habitaciones disponibles */
            Text(
                "Habitaciones disponibles",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            TextField(
                value = cantidadHabitaciones,
                onValueChange = { cantidadHabitaciones = it },
                placeholder = { Text("Ejemplo: 2") },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = customTextFieldColors(
                    containerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* Máximo de roomies */
            Text(
                "Máximo de roomies",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            TextField(
                value = maxRoomies,
                onValueChange = { maxRoomies = it },
                placeholder = { Text("Ejemplo: 3") },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = customTextFieldColors(
                    containerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            //  Servicios incluidos
            Text(
                "Servicios incluidos en el arriendo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            val servicios = listOf(
                "Internet", "Amoblado", "Lavadora", "Baño Privado", "Televisión", "Secadora",
                "Agua Caliente", "Cocina equipada", "Nevera compartida", "Parqueadero",
                "Acceso inclusivo", "Espacios comunes"
            )
            val seleccionadosServicios = remember {
                mutableStateListOf<String>().apply {
                    addAll(userProfile?.serviciosIncluidos?.split(",")?.map { it.trim() }
                        ?: emptyList())
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                servicios.forEach { servicio ->
                    val seleccionado = seleccionadosServicios.contains(servicio)
                    Chip(
                        text = servicio,
                        isSelected = seleccionado,
                        onClick = {
                            if (seleccionado) seleccionadosServicios.remove(servicio)
                            else seleccionadosServicios.add(servicio)
                            serviciosIncluidos = seleccionadosServicios.joinToString(", ")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reglas de convivencia
            Text(
                "Reglas de convivencia",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            val reglas = listOf(
                "Se aceptan visitas",
                "Hay horarios",
                "Se permiten fiestas",
                "Se aceptan mascotas",
                "Cada uno cocina",
                "Cada uno hace limpieza",
                "No hay problema por el ruido",
                "Visitas limitadas"
            )
            val seleccionadasReglas = remember {
                mutableStateListOf<String>().apply {
                    addAll(userProfile?.reglasConvivencia?.split(",")?.map { it.trim() }
                        ?: emptyList())
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                reglas.forEach { regla ->
                    val seleccionada = seleccionadasReglas.contains(regla)
                    Chip(
                        text = regla,
                        isSelected = seleccionada,
                        onClick = {
                            if (seleccionada) seleccionadasReglas.remove(regla)
                            else seleccionadasReglas.add(regla)
                            reglasConvivencia = seleccionadasReglas.joinToString(", ")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔘 Botón guardar
            Button(
                onClick = {
                    if (userData == null) {
                        Toast.makeText(context, "No se pudo obtener la información del usuario", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isSaving = true
                    val userId = userData.id ?: 0
                    viewModel.setUserId(userId)
                    viewModel.actualizarArriendo(arriendo.toDoubleOrNull())
                    viewModel.actualizarCantidadHabitaciones(cantidadHabitaciones.toIntOrNull() ?: 0)
                    viewModel.actualizarMaxRoomies(maxRoomies.toIntOrNull() ?: 1)
                    viewModel.actualizarBarrio(barrio)
                    viewModel.actualizarDescripcionLibre(descripcion)
                    viewModel.actualizarReglasConvivencia(reglasConvivencia)
                    viewModel.actualizarServiciosIncluidos(serviciosIncluidos)

                    viewModel.viewModelScope.launch {
                        try {
                            // 1️⃣ Eliminar fotos marcadas
                            if (fotosEliminadas.isNotEmpty()) {
                                viewModel.eliminarFotosResidencia(
                                    urls = fotosEliminadas,
                                    onSuccess = { fotosEliminadas.clear() },
                                    onError = { msg -> Log.e("PerfilEditar", msg) }
                                )
                            }

                            // 2️⃣ Subir nueva foto de perfil
                            if (fotoPerfilActualizada && nuevaFotoPerfilUri != null) {
                                viewModel.subirFotoPerfilAlFinal(
                                    context = context,
                                    onSuccess = { url ->
                                        viewModel.actualizarFotoPerfil(url)
                                    },
                                    onError = { msg -> Log.e("PerfilEditar", msg) }
                                )
                            }

                            // 3️⃣ Subir fotos de residencia nuevas
                            val fotosNuevas = fotosResidencia.filter { it.startsWith("content://") }
                            val nuevosArchivos = fotosNuevas.mapNotNull { uriToFile(context, it) }
                            if (nuevosArchivos.isNotEmpty()) {
                                viewModel.subirFotosResidencia(
                                    files = nuevosArchivos,
                                    onSuccess = { urls -> Log.d("PerfilEditar", "Fotos subidas: $urls") },
                                    onError = { msg -> Log.e("PerfilEditar", msg) }
                                )
                            }

                            // 4️⃣ Actualizar perfil
                            viewModel.editarPerfilTengo(
                                userProfile = userProfile,
                                context = context,
                                fechaNacimiento = userProfile?.fechaNacimiento,
                                onSuccess = {
                                    // 5️⃣ Recargar perfil en HomeViewModel
                                    homeViewModel.loadUserProfile(userId)

                                    // 6️⃣ Cerrar editor
                                    isSaving = false
                                    navController.popBackStack()
                                    Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                                },
                                onError = { msg ->
                                    isSaving = false
                                    Toast.makeText(context, "Error al actualizar: $msg", Toast.LENGTH_LONG).show()
                                }
                            )

                        } catch (e: Exception) {
                            isSaving = false
                            Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.e("PerfilEditar", "Stack trace: ${e.stackTraceToString()}")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("GUARDAR", style = MaterialTheme.typography.displaySmall)
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun Chip(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                shape = RoundedCornerShape(50)
            )
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.White
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black
        )
    }
}

@Composable
fun ResidenciaPhotosGrid(
    photos: List<String>,
    onAddPhoto: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val photoCount = photos.size
    val totalSlots = if (photoCount < 6) photoCount + 1 else 6
    val rows = if (photoCount <= 1) 1 else totalSlots / 3 + if (totalSlots % 3 != 0) 1 else 0
    var index = 0

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until 3) {
                    if (index < totalSlots) {
                        if (index < photoCount) {
                            AsyncImage(
                                model = photos[index],
                                contentDescription = "Foto residencia",
                                modifier = Modifier
                                    .size(100.dp, 140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, colors.primary, RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Cuadro para añadir nueva foto
                            Box(
                                modifier = Modifier
                                    .size(100.dp, 140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, colors.primary, RoundedCornerShape(12.dp))
                                    .clickable { onAddPhoto() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AddCircleOutline,
                                    contentDescription = "Agregar foto",
                                    tint = colors.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        index++
                    }
                }
            }
        }
    }
}

@Composable
fun ResidenciaPhotosGridEditar(
    photos: SnapshotStateList<String>,
    onAddPhoto: () -> Unit,
    onDeletePhoto: (Int) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val maxPhotos = 6

    // Calcular items a mostrar (fotos existentes + botón agregar si hay espacio)
    val totalItems = if (photos.size < maxPhotos) photos.size + 1 else photos.size

    // Dividir en filas de 3 columnas
    val rows = (totalItems + 2) / 3 // Redondeo hacia arriba

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        var currentIndex = 0

        repeat(rows) { rowIndex ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (colIndex in 0 until 3) {
                    if (currentIndex < totalItems) {
                        // Determinar qué mostrar en esta posición
                        if (currentIndex < photos.size) {
                            // 📸 Mostrar foto existente
                            val photoIndex = currentIndex // Guardar el índice REAL
                            Box(
                                modifier = Modifier
                                    .size(100.dp, 140.dp)
                            ) {
                                AsyncImage(
                                    model = photos[photoIndex],
                                    contentDescription = "Foto residencia $photoIndex",
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(2.dp, colors.primary, RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                // 🗑️ Botón eliminar - usar photoIndex (índice real)
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Eliminar foto",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(20.dp)
                                        .background(Color(0xCC000000), CircleShape)
                                        .clickable {
                                            onDeletePhoto(photoIndex) // Usar el índice REAL
                                        }
                                        .padding(4.dp)
                                )
                            }
                        } else {
                            // ➕ Mostrar botón agregar
                            Box(
                                modifier = Modifier
                                    .size(100.dp, 140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, colors.primary, RoundedCornerShape(12.dp))
                                    .clickable { onAddPhoto() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AddCircleOutline,
                                    contentDescription = "Agregar foto",
                                    tint = colors.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        currentIndex++
                    }
                }
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=800dp,dpi=420")
@Composable
fun PerfilTengoLugarScreenPreview() {
    RoomieMatchUTheme {
        PerfilTengoLugarScreen()
    }
}


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=800dp,dpi=420")
@Composable
fun PerfilEditarTengoLugarPreview() {
    RoomieMatchUTheme {
        PerfilEditarTengoLugarScreen()
    }
}
 */
