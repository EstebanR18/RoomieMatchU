package com.example.approomiematchu.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.approomiematchu.R
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.data.remote.dto.UserResponse
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun PerfilTengoLugarScreen(
    onBackClick: () -> Unit,
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null
) {
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        PerfilEditarScreenTengoLugar(
            userProfile = userProfile,
            userData = userData,
            onBackClick = { isEditing = false }
        )
    } else {
        PerfilVerScreenTengoLugar(
            onBackClick = onBackClick,
            onEditClick = { isEditing = true },
            userProfile = userProfile,
            userData = userData
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilVerScreenTengoLugar(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null
) {
    val scrollState = rememberScrollState()

    // 🔹 Calcular edad
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
        } catch (e: Exception) { null }
    }

    val nombreConEdad = remember(userProfile, userData) {
        val nombreReal = userData?.nombreCompleto ?: "Mi perfil"
        userProfile?.fechaNacimiento?.let { fechaNac ->
            val edad = calcularEdad(fechaNac)
            if (edad != null) "$nombreReal, $edad" else nombreReal
        } ?: nombreReal
    }

    val serviciosIncluidos = userProfile?.serviciosIncluidos
        ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
    val reglasConvivencia = userProfile?.reglasConvivencia
        ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0))
            .padding(24.dp)
    ) {
        // 🔹 Encabezado
        Box(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_atras_screens),
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterStart).size(28.dp).clickable { onBackClick() }
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
                modifier = Modifier.align(Alignment.CenterEnd).size(30.dp).clickable { onEditClick() }
            )
        }

        // 🔹 Contenido
        Column(
            modifier = Modifier.verticalScroll(scrollState).padding(bottom = 100.dp)
        ) {
            // Foto de perfil
            Row(
                modifier = Modifier.fillMaxWidth().height(180.dp),
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
            Text("Zona o barrio", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            PerfilChipRow(listOfNotNull(userProfile?.barrio ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Precio
            Text("Precio del arriendo", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            PerfilChipRow(listOfNotNull(userProfile?.arriendo?.let { "$${"%,.0f".format(it)}" } ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Habitaciones
            Text("Habitaciones disponibles", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            PerfilChipRow(listOfNotNull(userProfile?.cantidadHabitaciones?.toString() ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Máximo de roomies
            Text("Máximo de roomies", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            PerfilChipRow(listOfNotNull(userProfile?.maxRoomies?.toString() ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Servicios incluidos
            Text("Servicios incluidos en el arriendo", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            PerfilChipRow(serviciosIncluidos.ifEmpty { listOf("No especificado") })

            Spacer(modifier = Modifier.height(16.dp))

            // Reglas convivencia
            Text("Reglas de convivencia", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
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
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    // 📆 Calcular edad
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
        } catch (e: Exception) { null }
    }

    // 🧩 Estados editables
    var descripcion by remember { mutableStateOf(userProfile?.descripcionLibre ?: "") }
    var arriendo by remember { mutableStateOf(userProfile?.arriendo?.toString() ?: "") }
    var cantidadHabitaciones by remember { mutableStateOf(userProfile?.cantidadHabitaciones?.toString() ?: "") }
    var maxRoomies by remember { mutableStateOf(userProfile?.maxRoomies?.toString() ?: "") }

    var barrio by remember { mutableStateOf(userProfile?.barrio ?: "") }
    var serviciosIncluidos by remember { mutableStateOf(userProfile?.serviciosIncluidos ?: "") }
    var reglasConvivencia by remember { mutableStateOf(userProfile?.reglasConvivencia ?: "") }

    // Fotos residencia
    val fotosResidencia = remember { mutableStateListOf<String>().apply {
        addAll(userProfile?.fotosResidenciaUrls ?: emptyList())
    }}

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
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.ic_atras_screens),
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterStart).size(28.dp).clickable { onBackClick() }
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
            modifier = Modifier.verticalScroll(scrollState).padding(bottom = 100.dp)
        ) {
            // 📷 Foto de perfil
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (!userProfile?.fotoPerfil.isNullOrEmpty()) {
                    AsyncImage(
                        model = userProfile?.fotoPerfil,
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
                        .clickable { /* TODO cambiar foto */ }
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

            // 📸 Fotos residencia editables
            ResidenciaPhotosGridEditar(
                photos = fotosResidencia,
                onAddPhoto = { fotosResidencia.add("https://picsum.photos/200/300?${fotosResidencia.size}") },
                onEditPhoto = { /* TODO editar */ },
                onDeletePhoto = { index -> fotosResidencia.removeAt(index) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🧾 Descripción
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

            // 🏙️ Zona / barrio
            Text("Zona o barrio", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
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

            /* 💰 Precio del arriendo */
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

            /* 🏠 Habitaciones disponibles */
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

            /* 👥 Máximo de roomies */
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

            // 🧾 Servicios incluidos
            Text("Servicios incluidos en el arriendo", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            val servicios = listOf(
                "Internet", "Amoblado", "Lavadora", "Baño Privado", "Televisión", "Secadora",
                "Agua Caliente", "Cocina equipada", "Nevera compartida", "Parqueadero",
                "Acceso inclusivo", "Espacios comunes"
            )
            val seleccionadosServicios = remember { mutableStateListOf<String>().apply {
                addAll(userProfile?.serviciosIncluidos?.split(",")?.map { it.trim() } ?: emptyList())
            }}

            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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

            // 📋 Reglas de convivencia
            Text("Reglas de convivencia", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
            val reglas = listOf(
                "Se aceptan visitas", "Hay horarios", "Se permiten fiestas", "Se aceptan mascotas",
                "Cada uno cocina", "Cada uno hace limpieza", "No hay problema por el ruido"
            )
            val seleccionadasReglas = remember { mutableStateListOf<String>().apply {
                addAll(userProfile?.reglasConvivencia?.split(",")?.map { it.trim() } ?: emptyList())
            }}

            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                    // TODO: Implementar acción guardar (actualizar datos en backend)
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("GUARDAR", style = MaterialTheme.typography.displaySmall)
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
    photos: List<String>,
    onAddPhoto: () -> Unit,
    onEditPhoto: (Int) -> Unit,
    onDeletePhoto: (Int) -> Unit
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
                            Box(
                                modifier = Modifier
                                    .size(100.dp, 140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, colors.primary, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = photos[index],
                                    contentDescription = "Foto residencia",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                // Iconos de editar y eliminar
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar foto",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable { onEditPhoto(index) }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar foto",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable { onDeletePhoto(index) }
                                    )
                                }
                            }
                        } else {
                            // Cuadro para agregar foto
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
fun cuTextFieldColors(
    containerColor: Color,
    focusedIndicatorColor: Color,
    unfocusedIndicatorColor: Color,
    cursorColor: Color
): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        cursorColor = cursorColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor
    )
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
