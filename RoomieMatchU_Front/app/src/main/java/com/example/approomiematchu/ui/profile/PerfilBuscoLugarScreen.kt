package com.example.approomiematchu.ui.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.approomiematchu.R
import com.example.approomiematchu.data.remote.RetrofitClient
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.data.remote.dto.UserResponse
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.viewmodel.HomeViewModel
import com.example.approomiematchu.viewmodel.PerfilCuestionarioViewModel
import com.example.approomiematchu.viewmodel.PerfilCuestionarioViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun PerfilBuscoLugarScreen(
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null,
    navController: NavController
) {
    PerfilVerScreenBuscoLugar(
        userProfile = userProfile,
        userData = userData,
        navController = navController
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilVerScreenBuscoLugar(
    userProfile: PerfilResponse? = null,
    userData: UserResponse? = null,
    navController: NavController
) {
    val scrollState = rememberScrollState()

    // calcular edad sin remember para que se recalcule siempre con userProfile nuevo
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

    val nombreConEdad = run {
        val nombreReal = userData?.nombreCompleto ?: "Mi perfil"
        userProfile?.fechaNacimiento?.let { fechaNac ->
            val edad = calcularEdad(fechaNac)
            if (edad != null) "$nombreReal, $edad" else nombreReal
        } ?: nombreReal
    }

    val fumaTexto = if (userProfile?.fuma == true) "Fumo" else "No fumo"
    val mascotaTexto = if (userProfile?.mascota == true) "Tengo mascota" else "No tengo mascota"
    val alergiaTexto = when {
        userProfile?.alergico == true && !userProfile.detalleAlergia.isNullOrEmpty() -> "Alérgico a ${userProfile.detalleAlergia}"
        userProfile?.alergico == true -> "Tengo alergias"
        else -> "Sin alergias"
    }

    val habitosChips = listOf(fumaTexto, mascotaTexto, alergiaTexto)
    val serviciosChips = userProfile?.serviciosDeseados
        ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0))
            .padding(24.dp)
    ) {
        // Encabezado
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
                        navController.navigate(AppScreens.PerfilEditarBuscoLugar.route)
                    }
            )
        }

        // Contenido scroll
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp)
        ) {
            // Foto
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
                        painter = painterResource(id = R.drawable.imagen1),
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

            // Nombre + edad
            Text(
                text = nombreConEdad,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text(
                            "Descripción",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = userProfile?.descripcionLibre?.takeIf { it.isNotEmpty() }
                                ?: "Aún no tienes una descripción. ¡Agrega una para contar más sobre ti!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (userProfile?.descripcionLibre.isNullOrEmpty()) Color.Gray else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Género
            Text(
                "Género",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.genero))

            Spacer(modifier = Modifier.height(16.dp))

            // Hábitos
            Text(
                "Hábitos",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(habitosChips)

            Spacer(modifier = Modifier.height(16.dp))

            // Idioma
            Text(
                "Idioma",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.idioma?.takeIf { it.isNotEmpty() }
                ?: "No especificado"))

            Spacer(modifier = Modifier.height(16.dp))

            // Teléfono
            Text(
                "Teléfono",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.telefono ?: "No registrado"))

            Spacer(modifier = Modifier.height(16.dp))

            // Presupuesto
            Text(
                "Presupuesto máximo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.presupuesto?.let { "$${"%,.0f".format(it)}" }
                ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Tiempo estimado de estancia
            Text(
                "Tiempo estimado de estancia",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.tiempoEstancia ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Personas convivencia
            Text(
                "Personas con las que estaría dispuesto a vivir",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.personasConvivencia ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Zona o barrio
            Text(
                "Zona o barrio preferido",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.barrio ?: "No definido"))

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha de mudanza
            Text(
                "Fecha en la que necesita mudarse",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(listOfNotNull(userProfile?.fechaMudanza ?: "No definida"))

            Spacer(modifier = Modifier.height(16.dp))

            // Servicios deseados
            Text(
                "Servicios indispensables que busca",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PerfilChipRow(serviciosChips.ifEmpty { listOf("No especificado") })

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

// 🔹 Chips reutilizables (solo lectura)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilChipRow(items: List<String>) {
    if (items.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        FlowRow(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items.forEach { text ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilEditarScreenBuscoLugar(
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
    val scope = rememberCoroutineScope()

    // Campos editables inicializados con userProfile
    var descripcion by remember { mutableStateOf(userProfile?.descripcionLibre ?: "") }
    var presupuesto by remember { mutableStateOf(userProfile?.presupuesto?.toString() ?: "") }
    var barrio by remember { mutableStateOf(userProfile?.barrio ?: "") }
    var idioma by remember { mutableStateOf(userProfile?.idioma ?: "") }
    var telefono by remember { mutableStateOf(userProfile?.telefono ?: "") }

    // Foto de perfil
    var nuevaFotoPerfilUri by remember { mutableStateOf<Uri?>(null) }
    var fotoPerfilActualizada by remember { mutableStateOf(false) }

    val launcherFotoPerfil = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            nuevaFotoPerfilUri = it
            fotoPerfilActualizada = true
            viewModel.actualizarFotoPerfilLocal(it.toString())
        }
    }

    // 🔹 Selecciones iniciales (listas base)
    val generos = listOf("Femenino", "Masculino", "Otro")
    val zonas = listOf(
        "Usaquén", "Chapinero", "Santa Fe", "San Cristóbal", "Usme",
        "Tunjuelito", "Bosa", "Kennedy", "Fontibón", "Engativá",
        "Suba", "Barrios Unidos", "Teusaquillo", "Los Mártires",
        "Antonio Nariño", "Puente Aranda", "La Candelaria",
        "Rafael Uribe Uribe", "Ciudad Bolívar", "Sumapaz"
    )
    val tiemposEstancia = listOf("Menos de 6 meses", "6-12 meses", "Más de 1 año")
    val personasConvivencia = listOf("Menos de 3", "3-5 personas", "Más de 5")
    val fechasMudanza = listOf("Inmediato", "Próximo mes", "En 2 a 3 meses")

    val serviciosDisponibles = listOf(
        "Internet", "Agua Caliente", "Lavadora",
        "Secadora", "Amoblado básico", "Baño privado",
        "Televisión", "Cocina equipada", "Nevera compartida",
        "Parqueadero", "Espacios comunes", "Acceso inclusivo"
    )

    // 🔹 Selecciones recordadas
    val generoSeleccionado = remember { mutableStateOf(userProfile?.genero ?: "") }
    val fuma = remember { mutableStateOf(userProfile?.fuma ?: false) }
    val mascota = remember { mutableStateOf(userProfile?.mascota ?: false) }
    val alergico = remember { mutableStateOf(userProfile?.alergico ?: false) }
    var detalleAlergia by remember { mutableStateOf(userProfile?.detalleAlergia ?: "") }

    val zonaSeleccionada = remember { mutableStateOf(userProfile?.barrio ?: "") }
    val tiempoEstanciaSeleccionado = remember { mutableStateOf(userProfile?.tiempoEstancia ?: "") }
    val personasSeleccionadas = remember { mutableStateOf(userProfile?.personasConvivencia ?: "") }
    val fechaMudanzaSeleccionada = remember { mutableStateOf(userProfile?.fechaMudanza ?: "") }

    val serviciosSeleccionados = remember {
        mutableStateListOf<String>().apply {
            userProfile?.serviciosDeseados
                ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
                ?.let { addAll(it) }
        }
    }

    // 🔹 Cálculo de edad
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

    val nombreConEdad = remember(userProfile, userData) {
        val nombreReal = userData?.nombreCompleto ?: "Mi perfil"
        userProfile?.fechaNacimiento?.let { fechaNac ->
            val edad = calcularEdad(fechaNac)
            if (edad != null) "$nombreReal, $edad" else nombreReal
        } ?: nombreReal
    }

    fun editarPerfilDespuesDeFoto(userId: Long) {
        scope.launch {
            try {
                viewModel.editarPerfilBusco(
                    onSuccess = {
                        homeViewModel.loadUserProfile(userId)
                        isSaving = false
                        navController.popBackStack()
                        Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    },
                    onError = { msg ->
                        isSaving = false
                        Toast.makeText(context, "Error al actualizar: $msg", Toast.LENGTH_LONG).show()
                        Log.e("PerfilEditarBusco", "Error editando perfil: $msg")
                    }
                )
            } catch (e: Exception) {
                isSaving = false
                Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("PerfilEditarBusco", "Stack: ${e.stackTraceToString()}")
            }
        }
    }


    // 🔹 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0))
            .padding(24.dp)
    ) {
        // Encabezado
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
                        navController.navigate(AppScreens.PerfilBuscoLugar.route)
                    }
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

        // Contenido scrolleable
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
                        painter = painterResource(id = R.drawable.imagen1),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(5.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Icono de agregar/cambiar foto
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "Agregar o cambiar foto",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(36.dp)
                        .clickable { launcherFotoPerfil.launch("image/*") }
                )
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

            // 🔸 Descripción
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text(
                            "Descripción",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            placeholder = { Text("Aquí puedes agregar una descripción sobre ti o lo que buscas...") },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                            colors = customTextFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔸 Género
            Text(
                "Género",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                generos.forEach { genero ->
                    val selected = generoSeleccionado.value == genero
                    ChipToggle(genero, selected) { generoSeleccionado.value = genero }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Fuma / Mascota
            Text(
                "Hábitos",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ChipToggle("Fumo", fuma.value) { fuma.value = !fuma.value }
                ChipToggle("Tengo mascota", mascota.value) { mascota.value = !mascota.value }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Alergias
            Text(
                "Alergias",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ChipToggle("Soy alérgico", alergico.value) { alergico.value = !alergico.value }
            }
            if (alergico.value) {
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = detalleAlergia,
                    onValueChange = { detalleAlergia = it },
                    placeholder = { Text("Especifica tus alergias (ej. mariposas)") },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = customTextFieldColors(
                        containerColor = Color.White,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(50)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Idioma
            Text(
                "Idioma",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            TextField(
                value = idioma,
                onValueChange = { idioma = it },
                placeholder = { Text("Ejemplo: Español, Inglés") },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = customTextFieldColors(
                    containerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Teléfono
            Text(
                "Teléfono",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                placeholder = { Text("Número de contacto") },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = customTextFieldColors(
                    containerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Presupuesto
            Text(
                "Presupuesto máximo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            TextField(
                value = presupuesto,
                onValueChange = { presupuesto = it },
                placeholder = { Text("Ejemplo: 1.400.000") },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = customTextFieldColors(
                    containerColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            )


            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Tiempo estimado de estancia
            Text(
                "Tiempo estimado de estancia",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                tiemposEstancia.forEach { tiempo ->
                    ChipToggle(
                        tiempo,
                        tiempoEstanciaSeleccionado.value == tiempo
                    ) { tiempoEstanciaSeleccionado.value = tiempo }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Personas convivencia
            Text(
                "Personas con las que estarías dispuesto a vivir",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                personasConvivencia.forEach { grupo ->
                    ChipToggle(
                        grupo,
                        personasSeleccionadas.value == grupo
                    ) { personasSeleccionadas.value = grupo }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Zona o barrio
            Text(
                "Zona o barrio preferido",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                zonas.forEach { zona ->
                    ChipToggle(zona, zonaSeleccionada.value == zona) {
                        zonaSeleccionada.value = zona
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Fecha de mudanza
            Text(
                "Fecha en la que necesitas mudarte",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                fechasMudanza.forEach { fecha ->
                    ChipToggle(
                        fecha,
                        fechaMudanzaSeleccionada.value == fecha
                    ) { fechaMudanzaSeleccionada.value = fecha }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔸 Servicios indispensables
            Text(
                "Servicios indispensables que buscas",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                serviciosDisponibles.forEach { servicio ->
                    val selected = serviciosSeleccionados.contains(servicio)
                    ChipToggle(servicio, selected) {
                        if (selected) serviciosSeleccionados.remove(servicio)
                        else serviciosSeleccionados.add(servicio)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔸 Botón Guardar
            Button(
                onClick = {
                    if (userData == null) {
                        Toast.makeText(context, "No se pudo obtener la información del usuario", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isSaving = true
                    val userId = userData.id

                    viewModel.setUserId(userId)
                    viewModel.actualizarDescripcionLibre(descripcion)
                    viewModel.actualizarBarrio(zonaSeleccionada.value) // Usar la zona seleccionada
                    viewModel.actualizarIdioma(idioma)
                    viewModel.actualizarTelefono(telefono)
                    viewModel.actualizarPresupuesto(presupuesto.toDoubleOrNull())

                    viewModel.actualizarGenero(generoSeleccionado.value)
                    viewModel.actualizarFuma(fuma.value)
                    viewModel.actualizarMascota(mascota.value)
                    viewModel.actualizarAlergico(alergico.value, if (alergico.value) detalleAlergia else null)
                    viewModel.actualizarTiempoEstancia(tiempoEstanciaSeleccionado.value)
                    viewModel.actualizarPersonasConvivencia(personasSeleccionadas.value)
                    viewModel.actualizarFechaMudanza(fechaMudanzaSeleccionada.value)

                    val serviciosString = serviciosSeleccionados.joinToString(", ")
                    viewModel.actualizarServiciosDeseados(serviciosString)

                    viewModel.actualizarCampoFechaNacimiento(userProfile?.fechaNacimiento ?: "")

                    // flow: (1) subir foto si cambió, (2) editar perfil BUSCO, (3) reload homeViewModel, (4) popBackStack()
                    scope.launch {
                        try {
                            // 1️⃣ Subir nueva foto de perfil si cambió
                            if (fotoPerfilActualizada && nuevaFotoPerfilUri != null) {
                                viewModel.subirFotoPerfilAlFinal(
                                    context = context,
                                    onSuccess = { url ->
                                        viewModel.actualizarFotoPerfil(url)
                                        editarPerfilDespuesDeFoto(userId)
                                    },
                                    onError = { msg ->
                                        isSaving = false
                                        Toast.makeText(context, "Error al subir foto: $msg", Toast.LENGTH_LONG).show()
                                    }
                                )
                            } else {
                                editarPerfilDespuesDeFoto(userId)
                            }
                        } catch (e: Exception) {
                            isSaving = false
                            Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                            Log.e("PerfilEditarBusco", "Stack: ${e.stackTraceToString()}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(50)
            ) {
                if (isSaving) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                else Text("GUARDAR", style = MaterialTheme.typography.displaySmall)
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

// 🔹 Reusable chip toggle estilo visual actual
@Composable
fun ChipToggle(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(
                2.dp,
                if (selected) MaterialTheme.colorScheme.primary else Color.Gray,
                RoundedCornerShape(50)
            )
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.White)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Black
        )
    }
}

@Composable
fun customTextFieldColors(
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
fun PerfilScreenPreview() {
    RoomieMatchUTheme {
        PerfilBuscoLugarScreen()
    }
}


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=800dp,dpi=420")
@Composable
fun PerfilEditarPreview() {
    RoomieMatchUTheme {
        PerfilEditarScreen()
    }
}
 */
