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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilTengoLugarScreen(
    onBackClick: () -> Unit,
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null
) {
    val scrollState = rememberScrollState()

    // Función para calcular la edad
    fun calcularEdad(fechaNacimiento: String): Int? {
        return try {
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaNac = formato.parse(fechaNacimiento)
            val hoy = Calendar.getInstance()
            val nacimiento = Calendar.getInstance().apply { time = fechaNac }

            var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)

            // Ajustar si aún no ha pasado el cumpleaños este año
            if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
                edad--
            }

            edad
        } catch (e: Exception) {
            null // Retornar null si hay error en el parsing
        }
    }

    // Calcular nombre y edad
    val nombreConEdad = remember(userProfile, userData) {
        // Usar el nombre real de userData si está disponible
        val nombreReal = userData?.nombreCompleto ?: "Nombre Usuario"

        userProfile?.fechaNacimiento?.let { fechaNac ->
            val edad = calcularEdad(fechaNac)
            if (edad != null) {
                "$nombreReal, $edad"
            } else {
                nombreReal
            }
        } ?: nombreReal
    }

    // Extraer datos del perfil - sin valores por defecto
    val descripcion = userProfile?.descripcionLibre
    val barrio = userProfile?.barrio
    val precio = userProfile?.arriendo?.let { "$${it.toInt()}" }
    val habitaciones = userProfile?.cantidadHabitaciones?.toString()
    val maxRoomies = userProfile?.maxRoomies?.toString()
    val serviciosIncluidos = userProfile?.serviciosIncluidos
    val reglasConvivencia = userProfile?.reglasConvivencia

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0))
            .padding(24.dp)
    ) {
        // 🔹 Encabezado fijo (NO scrolleable)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            // Flecha de atrás (izquierda)
            Icon(
                painter = painterResource(id = R.drawable.ic_atras_screens),
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(28.dp)
                    .clickable { onBackClick() }
            )

            // Título centrado SIEMPRE
            Text(
                text = "PERFIL",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )

            // Iconos de la derecha
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ajustes",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Columna interna con scroll (contenido)
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(bottom = 80.dp) // espacio al final del scroll
        ) {
            // 🔹 Foto de perfil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // Usar foto real si está disponible
                if (!userProfile?.fotoPerfil.isNullOrEmpty()) {
                    AsyncImage(
                        model = userProfile?.fotoPerfil,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(
                                width = 5.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable { },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // No mostrar imagen por defecto si no hay foto
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(
                                width = 5.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Sin foto",
                            tint = Color.Gray,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre con edad (solo mostrar si no está vacío)
            if (nombreConEdad.isNotEmpty()) {
                Text(
                    text = nombreConEdad,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Fotos de la residencia (solo mostrar si hay datos)
            userProfile?.fotosResidenciaUrls?.let { fotosResidencia ->
                if (fotosResidencia.isNotEmpty()) {
                    ResidenciaPhotosGrid(
                        photos = fotosResidencia,
                        onAddPhoto = { /* acción para agregar foto */ }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 🔹 Descripción - SIEMPRE mostrar el contenedor (vacío si no hay datos)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = descripcion ?: "Agrega una breve descripción del lugar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Barrio (solo mostrar si existe)
            barrio?.let {
                Text(
                    text = barrio,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Estilo de vida (solo mostrar si hay datos)
            val habitosChips = mutableListOf<String>()
            userProfile?.fuma?.let { if (!it) habitosChips.add("No fumo") }
            userProfile?.mascota?.let { if (it) habitosChips.add("Tengo mascotas") else habitosChips.add("Estoy dispuesto a vivir con mascotas") }
            userProfile?.alergico?.let { if (!it) habitosChips.add("Sin alergias") }

            if (habitosChips.isNotEmpty()) {
                Text(
                    text = "Estilo de vida",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    habitosChips.forEach { Chip(it) }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Precio (solo mostrar si existe)
            precio?.let {
                Text(
                    text = "Precio del arrendamiento",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(it)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Habitaciones disponibles (solo mostrar si existe)
            habitaciones?.let {
                Text(
                    text = "Habitaciones disponibles",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(it)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Máximo de roomies (solo mostrar si existe)
            maxRoomies?.let {
                Text(
                    text = "Máximo de roomies",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Chip(it)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Servicios incluidos (solo mostrar si hay datos)
            serviciosIncluidos?.let { serviciosStr ->
                val servicios = serviciosStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                if (servicios.isNotEmpty()) {
                    Text(
                        text = "Servicios incluidos en el arriendo",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        servicios.forEach { Chip(it) }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Reglas de convivencia (solo mostrar si hay datos)
            reglasConvivencia?.let { reglasStr ->
                val reglas = reglasStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                if (reglas.isNotEmpty()) {
                    Text(
                        text = "Reglas de convivencia",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        reglas.forEach { Chip(it) }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // espacio final visible
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilTengoLugarScreenEditar(
    onBackClick: () -> Unit,
    userProfile: PerfilResponse? = null
) {
    var isEditing by remember { mutableStateOf(false) }
    var descripcion by remember { mutableStateOf("Soy Laura y busco apartamento en Chapinero.") }
    var precio by remember { mutableStateOf("$600.000") }
    var personas by remember { mutableStateOf("3") }

    val zonas = listOf(
        "Usaquén", "Chapinero", "Santa Fe", "San Cristóbal", "Usme",
        "Tunjuelito", "Bosa", "Kennedy", "Fontibón", "Engativá",
        "Suba", "Barrios Unidos", "Teusaquillo", "Los Mártires",
        "Antonio Nariño", "Puente Aranda", "La Candelaria",
        "Rafael Uribe Uribe", "Ciudad Bolívar", "Sumapaz"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0))
            .padding(24.dp)
    ) {
        // 🔹 Encabezado fijo
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
                    .clickable { onBackClick() }
            )

            Text(
                text = "EDITAR PERFIL",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )

            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Ajustes",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp)
            )
        }

        //  Scrollable content
        Column(
            modifier = Modifier
                .verticalScroll(scrollState, enabled = true)
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            // Foto con ícono cámara
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.imagen2),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Agregar foto",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .clickable { /* acción para cambiar foto */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                "Pablo, 20",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fotos de la residencia (editable)
            val fotosResidencia = remember { mutableStateListOf(
                "https://picsum.photos/200/300",
                "https://picsum.photos/201/300"
            )}

            ResidenciaPhotosGridEditar(
                photos = fotosResidencia,
                onAddPhoto = { /* agregar foto */ },
                onEditPhoto = { index -> /* editar foto fotosResidencia[index] */ },
                onDeletePhoto = { index -> fotosResidencia.removeAt(index) }
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Descripción editable con estilo de Card
            var isEditing by remember { mutableStateOf(false) }
            var descripcion by remember { mutableStateOf("Soy Pablo y tengo apartamento para compartir en Chapinero amoblado.") }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (isEditing) {
                            TextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                                colors = cuTextFieldColors(
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            Text(
                                text = descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar descripción",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .clickable { isEditing = !isEditing }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Zona preferida
            Text(
                "Zona preferida",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                zonas.forEach { zona ->
                    Chip(zona)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Campos (mismos del perfil principal)
            Text(
                "Estilo de vida",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "No fumo", "Si fumo",
                    "No estoy dispuesto a vivir con mascotas", "Estoy dispuesto a vivir con mascotas",
                    "Sin alergias", "Con alergias",
                    "No tengo mascotas", "Tengo mascotas"
                ).forEachIndexed { index, item ->
                    Chip(item)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Precio editable
            Text(
                "Precio del arrendamiento",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            TextField(
                value = precio,
                onValueChange = { precio = it },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = cuTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Habitaciones editable
            Text(
                "Habitaciones disponibles",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            TextField(
                value = personas,
                onValueChange = { personas = it },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = cuTextFieldColors(
                    containerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Servicios incluidos en el arriendo
            Text(
                "Servicios incluidos en el arriendo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "Internet", "Amoblado", "Lavadora",
                    "Baño Privado", "Televisión", "Secadora",
                    "Agua Caliente", "Cocina equipada",
                    "Nevera compartida", "Parqueadero",
                    "Acceso inclusivo", "Espacios comunes (sala, comedor)"
                ).forEach { Chip(it) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reglas básicas de la casa
            Text(
                "Reglas básicas de la casa",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            val reglasSeleccionadas = remember { mutableStateListOf<String>() }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "Se aceptan visitas",
                    "Hay horarios",
                    "Se permiten las fiestas",
                    "Se aceptan mascotas",
                    "Cada uno cocina",
                    "Cada uno hace limpieza",
                    "No hay problema por el ruido"
                )
                }

            Spacer(modifier = Modifier.height(32.dp))



            // Botón guardar
            Button(
                onClick = { /* guardar cambios */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("GUARDAR", style = MaterialTheme.typography.displaySmall)
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
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
