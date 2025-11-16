package com.example.approomiematchu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.approomiematchu.R
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileform.presentation.TipoPerfil
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme
import com.example.approomiematchu.util.calculateAgeFromIso
import com.example.approomiematchu.viewmodel.HomeViewModel
import com.example.approomiematchu.viewmodel.MatchViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    matchViewModel: MatchViewModel,
    userId: Long?
) {

    val homeState by homeViewModel.uiState.collectAsState()
    val userProfile by homeViewModel.userProfile.collectAsState()
    val perfilActual by matchViewModel.perfilActual.collectAsState()

    // Si no hay userId, redirigir al login
    if (userId == null) {
        LaunchedEffect(Unit) {
            navController.navigate(AppScreens.LandingScreen.route) {
                popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
            }
        }
        return
    }

    // Mostrar loading si está cargando
    if (homeState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    // Mostrar error si hay
    homeState.error?.let { error ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    homeViewModel.clearError()
                    homeViewModel.loadUserProfile(userId)
                }) {
                    Text("Reintentar")
                }
            }
        }
        return
    }

    // Mostrar el home según el tipo de perfil
    when (homeState.tipoPerfil) {
        TipoPerfil.BUSCO_LUGAR -> HomeBuscoCasaScreen(
            perfil = perfilActual,
            onDescriptionClick = {
                NavigationUtils.navigateToDescription(navController, homeState.tipoPerfil)
            },
            onProfileClick = {
                NavigationUtils.navigateToProfile(navController, homeState.tipoPerfil)
            },
            onNext = { matchViewModel.siguiente() },
            onBack = { matchViewModel.atras() },
            onLike = { matchViewModel.like() },
            onReject = { matchViewModel.rechazar() }
        )

        TipoPerfil.TENGO_LUGAR -> HomeTengoCasaScreen(
            perfil = perfilActual,
            onDescriptionClick = {
                NavigationUtils.navigateToDescription(navController, homeState.tipoPerfil)
            },
            onProfileClick = {
                NavigationUtils.navigateToProfile(navController, homeState.tipoPerfil)
            },
            onNext = { matchViewModel.siguiente() },
            onBack = { matchViewModel.atras() },
            onLike = { matchViewModel.like() },
            onReject = { matchViewModel.rechazar() }
        )

        else -> {
            // Si no tiene perfil, redirigir al cuestionario
            LaunchedEffect(Unit) {
                navController.navigate(AppScreens.CuestionarioRol.route) {
                    popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
                }
            }
            // Mientras tanto mostrar loading
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD2D0D0)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

/* -------------------- TENGO CASA - PANTALLA PRINCIPAL -------------------- */
@Composable
fun HomeTengoCasaScreen(
    perfil: PerfilResponse?,
    onDescriptionClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onLike: () -> Unit,
    onReject: () -> Unit
) {

    if (perfil == null) {
        NoMoreProfilesScreen()
        return
    }

    val edad = calculateAgeFromIso(perfil.fechaNacimiento)

    RoomieMatchUTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {
            // Contenido principal centrado verticalmente
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // 🔹 CENTRA verticalmente
            ) {
                TopBar(onProfileClick = onProfileClick)

                // 🔹 Imagen principal (carrusel adaptativo)
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val imageHeight = (screenHeight * 0.55f).coerceIn(350.dp, 600.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // CARRUSEL → solo fotoPerfil
                        ImageCarouselUrl(
                            images = listOfNotNull(perfil.fotoPerfil),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                        )

                        // Información debajo del carrusel
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFD2D0D0))
                                .padding(16.dp)
                                .clickable { onDescriptionClick() }
                        ) {
                            Text(
                                "${perfil.genero ?: "Usuario"}, ${edad ?: "?"}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                perfil.barrio ?: "Barrio desconocido",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                perfil.descripcionLibre ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                BottomControls(onBack, onLike, onReject)
            }
        }
    }
}

/* -------------------- TENGO CASA - DESCRIPCIÓN -------------------- */
@Composable
fun DescriptionTengoCasaScreen(
    perfil: PerfilResponse?,
    onBackClick: () -> Unit
) {
    RoomieMatchUTheme {

        val edad = calculateAgeFromIso(perfil?.fechaNacimiento)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {
            Spacer(Modifier.height(24.dp))

            DetailHeader(onBackClick = onBackClick)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = 100.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                    .zIndex(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val imageHeight = (screenHeight * 0.45f).coerceIn(300.dp, 500.dp)

                // CARD PRINCIPAL
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFFD2D0D0))
                        .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                ) {
                    Column {

                        // Carrusel (NO scrollea)
                        ImageCarouselUrl(
                            images = listOfNotNull(perfil?.fotoPerfil),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        )

                        // CONTENIDO SCROLLEABLE
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFD2D0D0))
                                .padding(20.dp)
                        ) {

                            Text(
                                "${perfil?.genero ?: "-"}, ${edad ?: "--"}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                perfil?.barrio ?: "-",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(10.dp))

                            // PREFERENCIAS
                            ChipSection(
                                title = "Preferencias",
                                items = listOf(
                                    perfil?.presupuesto?.let { "Presupuesto: $$it" },
                                    perfil?.tipoHabitacion,
                                    perfil?.tiempoEstancia,
                                    perfil?.fechaMudanza?.let { "Mudanza: $it" }
                                )
                            )

                            // SERVICIOS DESEADOS → A CHIPS
                            ChipSection(
                                title = "Servicios deseados",
                                items = perfil?.serviciosDeseados
                                    ?.split(",")
                                    ?.map { it.trim() }
                                    ?: emptyList()
                            )

                            /*
                            // HÁBITOS → CHIPS
                            ChipSection(
                                title = "Hábitos",
                                items = perfil?.habitos
                                    ?.split(",")
                                    ?.map { it.trim() }
                                    ?: emptyList()
                            )
                             */

                            // OTROS
                            ChipSection(
                                title = "Otros",
                                items = listOf(
                                    if (perfil?.mascota == true) "Mascota" else "Sin mascota",
                                    if (perfil?.fuma == true) "Fuma" else "No fuma",
                                    if (perfil?.alergico == true) "Alergia: ${perfil.detalleAlergia}" else null
                                )
                            )

                        }
                    }
                }
            }
        }
    }
}

//* -------------------- BUSCO CASA - PANTALLA PRINCIPAL -------------------- */
@Composable
fun HomeBuscoCasaScreen(
    perfil: PerfilResponse?,
    onDescriptionClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onLike: () -> Unit,
    onReject: () -> Unit
) {

    if (perfil == null) {
        NoMoreProfilesScreen()
        return
    }

    // Edad calculada
    val edad = calculateAgeFromIso(perfil.fechaNacimiento)

    RoomieMatchUTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {
            // Contenido principal centrado verticalmente
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // 🔹 Centrado verticalmente
            ) {
                TopBar(onProfileClick = onProfileClick)

                // 🔹 Imagen principal (carrusel adaptativo)
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val imageHeight = (screenHeight * 0.55f).coerceIn(350.dp, 600.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFFD2D0D0))
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Carrusel
                        ImageCarouselUrl(
                            images = perfil.fotosResidenciaUrls ?: emptyList(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        )

                        // 🔹 Información debajo del carrusel + foto de perfil
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFD2D0D0))
                                .padding(16.dp)
                                .clickable { onDescriptionClick() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        "${perfil.genero ?: "Usuario"}, ${edad ?: "?"}",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        perfil.barrio ?: "Barrio desconocido",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        perfil.descripcionLibre ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                // FOTO DE PERFIL
                                AsyncImage(
                                    model = perfil.fotoPerfil,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(CircleShape)
                                        .border(
                                            3.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                BottomControls(onBack, onLike, onReject)
            }
        }
    }
}

/* -------------------- BUSCO CASA - DESCRIPCIÓN -------------------- */
@Composable
fun DescriptionBuscoCasaScreen(
    perfil: PerfilResponse?,
    onBackClick: () -> Unit
) {
    RoomieMatchUTheme {

        val edad = calculateAgeFromIso(perfil?.fechaNacimiento)
        val imageUrls = perfil?.fotosResidenciaUrls ?: listOfNotNull(perfil?.fotoPerfil)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {

            Spacer(Modifier.height(24.dp))

            DetailHeader(onBackClick = onBackClick)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = 100.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // CARD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFFD2D0D0))
                        .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                        .zIndex(1f)
                ) {

                    Column {

                        // 🔵 Carrusel NO scrollea
                        ImageCarouselUrl(
                            images = imageUrls,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(340.dp)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        )

                        // Contenido SCROLLEABLE
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFD2D0D0))
                                .padding(20.dp)
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = perfil?.fotoPerfil,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .border(
                                            3.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        ),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(Modifier.width(16.dp))

                                Column {
                                    Text(
                                        "${perfil?.genero ?: "-"}, ${edad ?: "--"}",
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        perfil?.barrio ?: "-",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }

                            Spacer(Modifier.height(20.dp))

                            // DETALLES DEL LUGAR
                            ChipSection(
                                title = "Detalles del lugar",
                                items = listOf(
                                    perfil?.arriendo?.let { "Arriendo: $$it" },
                                    perfil?.cantidadHabitaciones?.let { "Habitaciones: $it" },
                                    perfil?.maxRoomies?.let { "Máx. roomies: $it" }
                                )
                            )

                            // REGLAS DE CONVIVENCIA → CHIPS
                            ChipSection(
                                title = "Reglas de convivencia",
                                items = perfil?.reglasConvivencia
                                    ?.split(",")
                                    ?.map { it.trim() }
                                    ?: emptyList()
                            )

                            // SERVICIOS INCLUIDOS → CHIPS
                            ChipSection(
                                title = "Servicios incluidos",
                                items = perfil?.serviciosIncluidos
                                    ?.split(",")
                                    ?.map { it.trim() }
                                    ?: emptyList()
                            )

                            /*
                            // HÁBITOS PERSONAL → CHIPS
                            ChipSection(
                                title = "Hábitos",
                                items = perfil?.habitos
                                    ?.split(",")
                                    ?.map { it.trim() }
                                    ?: emptyList()
                            )
                             */

                            // OTROS
                            ChipSection(
                                title = "Otros",
                                items = listOf(
                                    if (perfil?.mascota == true) "Mascota" else "Sin mascota",
                                    if (perfil?.fuma == true) "Fuma" else "No fuma",
                                    if (perfil?.alergico == true) "Alergia: ${perfil.detalleAlergia}" else null
                                )
                            )

                        }
                    }
                }
            }
        }
    }
}

/* ---------- Reutilizables ---------- */

@Composable
fun TopBar(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.size(35.dp))

        Text(
            text = "ROOMIE\nMATCH U",
            style = MaterialTheme.typography.displayLarge.copy(lineHeight = 21.sp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Perfil",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(35.dp)
                .clickable { onProfileClick() }
        )
    }
}

@Composable
fun ImageCarouselUrl(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    var index by remember { mutableStateOf(0) }

    if (images.isEmpty()) {
        Box(
            modifier = modifier
                .background(Color.LightGray)
                .height(300.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text("Sin fotos")
        }
        return
    }

    Box(modifier) {
        AsyncImage(
            model = images[index],
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { index = (index + 1) % images.size },
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        ) {
            images.forEachIndexed { i, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .height(4.dp)
                        .width(40.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (i == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surface
                        )
                )
            }
        }
    }
}
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(50))
            .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ChipSection(title: String, items: List<String?>) {
    val filtered = items.filterNotNull()
    if (filtered.isNotEmpty()) {
        SectionHeader(title)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            filtered.forEach { Chip(it) }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* -------------------- COMPONENTES DE APOYO -------------------- */

@Composable
fun DetailHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD2D0D0))
            .padding(top = 24.dp, bottom = 12.dp)
            .zIndex(2f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_atras_screens),
            contentDescription = "Atrás",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterStart)
                .clickable { onBackClick() }
                .padding(start = 16.dp)
        )

        Text(
            text = "ROOMIE\nMATCH U",
            style = MaterialTheme.typography.displayLarge.copy(lineHeight = 21.sp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun BottomControls(onBack: () -> Unit, onLike: () -> Unit, onReject: () -> Unit) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val iconSize = (screenWidth * 0.15f).coerceIn(50.dp, 80.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_atras),
            contentDescription = "Atrás",
            modifier = Modifier
                .size(iconSize)
                .clickable { onBack() }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_aceptar),
            contentDescription = "Aceptar",
            modifier = Modifier
                .size(iconSize)
                .clickable { onLike() }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_rechazar),
            contentDescription = "Rechazar",
            modifier = Modifier
                .size(iconSize)
                .clickable { onReject() }
        )
    }
}

@Composable
fun NoMoreProfilesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2D0D0)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay más perfiles 🥲",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CircleIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(color)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
    )
}

/*
@Composable
fun PerfilChipRow(items: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { label ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(label, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

 */

/*
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,dpi=420")
@Composable
fun PreviewTengoCasaHome() {
    RoomieMatchUTheme {
        HomeTengoCasaScreen(onDescriptionClick = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=410dp,height=860dp,dpi=420")
@Composable
fun PreviewTengoCasaDescripcion() {
    RoomieMatchUTheme {
        DescriptionTengoCasaScreen(onBackClick = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=1000dp,dpi=420")
@Composable
fun PreviewBuscoCasaHome() {
    RoomieMatchUTheme {
        HomeBuscoCasaScreen(onDescriptionClick = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,dpi=420")
@Composable
fun PreviewBuscoCasaDescripcion() {
    RoomieMatchUTheme {
        DescriptionBuscoCasaScreen()
    }
}

 */
