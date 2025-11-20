package com.example.approomiematchu.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.approomiematchu.R
import com.example.approomiematchu.data.remote.dto.PerfilResponse
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileform.presentation.TipoPerfil
import com.example.approomiematchu.ui.state.PerfilAnimState
import com.example.approomiematchu.ui.state.PerfilUIState
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme
import com.example.approomiematchu.util.calculateAgeFromIso
import com.example.approomiematchu.viewmodel.HomeViewModel
import com.example.approomiematchu.viewmodel.MatchViewModel
import com.example.approomiematchu.viewmodel.SwipeDirection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberNotification(): Triple<SnackbarHostState, CoroutineScope, suspend (String) -> Unit> {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val show: suspend (String) -> Unit = { msg ->
        hostState.showSnackbar(
            message = msg,
            withDismissAction = true
        )
    }

    return Triple(hostState, scope, show)
}

@Composable
fun HomeScreen(
    reloadFlag: Boolean,
    navController: NavController,
    homeViewModel: HomeViewModel,
    matchViewModel: MatchViewModel,
    userId: Long?
) {
    val homeState by homeViewModel.uiState.collectAsState()
    val matchState by matchViewModel.uiState.collectAsState()
    val matchSwipe by matchViewModel.swipeDirection.collectAsState()
    val animationKey by matchViewModel.animationKey.collectAsState()

    // Sin sesión
    if (userId == null) {
        LaunchedEffect(Unit) {
            navController.navigate(AppScreens.LandingScreen.route) {
                popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
            }
        }
        return
    }

    LaunchedEffect(reloadFlag) {
        if (reloadFlag) {
            homeViewModel.loadUserProfile(userId)
            matchViewModel.cargarPerfiles(userId)
        }
    }

    // Loading
    if (homeState.isLoading) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        return
    }

    // Error
    homeState.error?.let { error ->
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error: $error")
        }
        return
    }

    // SIN PERFIL → MANDAR A CUESTIONARIO
    if (homeState.tipoPerfil == TipoPerfil.NONE) {

        LaunchedEffect("no-perfil") {
            navController.navigate(AppScreens.CuestionarioRol.route) {
                popUpTo(AppScreens.HomeScreen.route) { inclusive = true }
            }
        }

        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        return
    }

    // Perfil BUSCO
    if (homeState.tipoPerfil == TipoPerfil.BUSCO_LUGAR) {
        HomeBuscoCasaScreen(
            state = matchState,
            swipeDirection = matchSwipe,
            animationKey = animationKey,
            onDescriptionClick = {
                NavigationUtils.navigateToDescription(navController, TipoPerfil.BUSCO_LUGAR)
            },
            onProfileClick = {
                NavigationUtils.navigateToProfile(navController, TipoPerfil.BUSCO_LUGAR)
            },
            onNext = { matchViewModel.siguiente() },
            onBack = { matchViewModel.atras() },
            onLike = { matchViewModel.like() },
            onReject = { matchViewModel.rechazar() }
        )
        return
    }

    // Perfil TENGO
    if (homeState.tipoPerfil == TipoPerfil.TENGO_LUGAR) {
        HomeTengoCasaScreen(
            state = matchState,
            swipeDirection = matchSwipe,
            animationKey = animationKey,
            onDescriptionClick = {
                NavigationUtils.navigateToDescription(navController, TipoPerfil.TENGO_LUGAR)
            },
            onProfileClick = {
                NavigationUtils.navigateToProfile(navController, TipoPerfil.TENGO_LUGAR)
            },
            onNext = { matchViewModel.siguiente() },
            onBack = { matchViewModel.atras() },
            onLike = { matchViewModel.like() },
            onReject = { matchViewModel.rechazar() }
        )
    }
}

/* -------------------- COMPONENTES REUTILIZABLES -------------------- */

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

    Box(modifier = modifier) {
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

/* -------------------- PERFIL CARD + ANIMACIÓN -------------------- */

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PerfilCardAnimated(
    state: PerfilUIState,
    swipeDirection: SwipeDirection,
    animationKey: Int,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier,
    contentPerfil: @Composable (PerfilResponse) -> Unit,
    contentLoading: @Composable () -> Unit,
    contentEmpty: @Composable () -> Unit
) {
    val target = state

    LaunchedEffect(swipeDirection) {
        if (swipeDirection != SwipeDirection.NONE) {
            onAnimationEnd()   // Solo se activa con swipe real
        }
    }

    AnimatedContent(
        targetState = state,
        modifier = modifier,
        transitionSpec = {
            val duration = 450

            val enterOffset = { fullWidth: Int ->
                when (swipeDirection) {
                    SwipeDirection.RIGHT -> fullWidth
                    SwipeDirection.LEFT -> -fullWidth
                    else -> fullWidth / 6
                }
            }

            val exitOffset = { fullWidth: Int ->
                when (swipeDirection) {
                    SwipeDirection.RIGHT -> -fullWidth
                    SwipeDirection.LEFT -> fullWidth
                    else -> -fullWidth / 6
                }
            }

            (slideInHorizontally(
                animationSpec = tween(duration),
                initialOffsetX = enterOffset
            ) + fadeIn()) with

                    (slideOutHorizontally(
                        animationSpec = tween(duration),
                        targetOffsetX = exitOffset
                    ) + fadeOut())
        }
    ) { anim ->
        when (anim) {
            PerfilUIState.Loading -> contentLoading()
            PerfilUIState.Empty -> contentEmpty()
            is PerfilUIState.Data -> contentPerfil(anim.perfil)
        }
    }
}

/* -------------------- TENGO CASA - PANTALLA PRINCIPAL -------------------- */
@Composable
fun HomeTengoCasaScreen(
    state: PerfilUIState,
    swipeDirection: SwipeDirection,
    animationKey: Int,
    onDescriptionClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onLike: () -> Unit,
    onReject: () -> Unit
) {

    RoomieMatchUTheme {

        val (snackbarHost, scope, notify) = rememberNotification()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar(onProfileClick = onProfileClick)

                PerfilCardAnimated(
                    state = state,
                    swipeDirection = swipeDirection,
                    animationKey = animationKey,
                    modifier = Modifier.fillMaxWidth(),
                    contentPerfil = { perfil ->
                        HomeTengoCasaCard(perfil, onDescriptionClick)
                    },
                    contentLoading = {
                        CircularProgressIndicator()
                    },
                    contentEmpty = {
                        NoMoreProfilesScreen()
                    },
                    onAnimationEnd = { onNext() }
                )

                Spacer(Modifier.height(40.dp))

                BottomControls(
                    onBack = {
                        scope.launch {
                            notify("↩️ Volviste al perfil anterior")
                        }
                        onBack()
                    },
                    onLike = {
                        scope.launch {
                            notify("✨ ¡Solicitud enviada! Tu interés ha sido registrado.")
                        }
                        onLike()
                    },
                    onReject = {
                        scope.launch {
                            notify("⛔ Perfil descartado. Buscando mejores opciones…")
                        }
                        onReject()
                    }
                )

            }

            SnackbarHost(
                hostState = snackbarHost,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            )

        }
    }
}

@Composable
fun HomeTengoCasaCard(
    perfil: PerfilResponse,
    onDescriptionClick: () -> Unit
) {
    val edad = calculateAgeFromIso(perfil.fechaNacimiento)
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

            ImageCarouselUrl(
                images = listOfNotNull(perfil.fotoPerfil),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD2D0D0))
                    .padding(16.dp)
                    .clickable { onDescriptionClick() }
            ) {
                Text(
                    "${perfil.usuario ?: "Usuario"}, ${edad ?: "?"}",
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
            // usamos la misma separación que el Home (top = 24.dp en TopBar)
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(Modifier.height(24.dp))
                DetailHeader(onBackClick = onBackClick)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    val imageHeight = (screenHeight * 0.45f).coerceIn(300.dp, 500.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color(0xFFD2D0D0))
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(30.dp)
                            )
                    ) {
                        Column {
                            ImageCarouselUrl(
                                images = listOfNotNull(perfil?.fotoPerfil),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(imageHeight)
                                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                            )

                            // contenido dentro de la tarjeta (chips)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFD2D0D0))
                                    .padding(20.dp)
                            ) {
                                Text(
                                    "${perfil?.usuario ?: "-"}, ${edad ?: "--"}",
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

                                // Preferencias (chips agrupados)
                                ChipSection(
                                    title = "Preferencias",
                                    items = listOf(
                                        perfil?.presupuesto?.let { "Presupuesto: $$it" },
                                        perfil?.tipoHabitacion,
                                        perfil?.tiempoEstancia,
                                        perfil?.fechaMudanza?.let { "Mudanza: $it" }
                                    )
                                )

                                // Servicios deseados → CHIPs
                                ChipSection(
                                    title = "Servicios deseados",
                                    items = perfil?.serviciosDeseados
                                        ?.split(",")
                                        ?.map { it.trim() }
                                        ?: emptyList()
                                )

                                // Hábitos → CHIPs (si quieres mostrar)
                                ChipSection(
                                    title = "Hábitos",
                                    items = perfil?.habitos
                                        ?.split(",")
                                        ?.map { it.trim() }
                                        ?: emptyList()
                                )

                                // Otros
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
}

/* -------------------- BUSCO CASA - PANTALLA PRINCIPAL -------------------- */
@Composable
fun HomeBuscoCasaScreen(
    state: PerfilUIState,
    swipeDirection: SwipeDirection,
    animationKey: Int,
    onDescriptionClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onLike: () -> Unit,
    onReject: () -> Unit
) {
    RoomieMatchUTheme {

        val (snackbarHost, scope, notify) = rememberNotification()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBar(onProfileClick = onProfileClick)

                // Animación
                PerfilCardAnimated(
                    state = state,
                    swipeDirection = swipeDirection,
                    animationKey = animationKey,
                    modifier = Modifier.fillMaxWidth(),
                    contentPerfil = { perfil ->
                        HomeBuscoCasaCard(perfil, onDescriptionClick)
                    },
                    contentLoading = {
                        CircularProgressIndicator()
                    },
                    contentEmpty = {
                        NoMoreProfilesScreen()
                    },
                    onAnimationEnd = { onNext() }
                )

                Spacer(Modifier.height(40.dp))

                BottomControls(
                    onBack = {
                        scope.launch {
                            notify("↩️ Volviste al perfil anterior")
                        }
                        onBack()
                    },
                    onLike = {
                        scope.launch {
                            notify("✨ ¡Solicitud enviada! Tu interés ha sido registrado.")
                        }
                        onLike()
                    },
                    onReject = {
                        scope.launch {
                            notify("⛔ Perfil descartado. Buscando mejores opciones…")
                        }
                        onReject()
                    }
                )

            }

            SnackbarHost(
                hostState = snackbarHost,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            )

        }
    }
}

@Composable
fun HomeBuscoCasaCard(
    perfil: PerfilResponse,
    onDescriptionClick: () -> Unit
) {
    val edad = calculateAgeFromIso(perfil.fechaNacimiento)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = (screenHeight * 0.55f).coerceIn(350.dp, 600.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // CARD COMPLETA
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFFD2D0D0))
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {

            // CARRUSEL
            ImageCarouselUrl(
                images = perfil.fotosResidenciaUrls ?: emptyList(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
            )

            // INFO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD2D0D0))
                    .padding(16.dp)
                    .clickable { onDescriptionClick() }
            ) {
                Text(
                    "${perfil.usuario}, $edad",
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

        // ⭐ FOTO DE PERFIL SUPERPUESTA SOBRE EL CARRUSEL
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-10).dp, y = (-75).dp)
        ) {
            AsyncImage(
                model = perfil.fotoPerfil,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize() // clave para que llene el círculo
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
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
            Column {
                Spacer(Modifier.height(24.dp))
                DetailHeader(onBackClick = onBackClick)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color(0xFFD2D0D0))
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(30.dp)
                            )
                    ) {
                        Column {
                            ImageCarouselUrl(
                                images = imageUrls,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(340.dp)
                                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                            )

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
                                            "${perfil?.usuario ?: "-"}, ${edad ?: "--"}",
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

                                ChipSection(
                                    title = "Detalles del lugar",
                                    items = listOf(
                                        perfil?.arriendo?.let { "Arriendo: $$it" },
                                        perfil?.cantidadHabitaciones?.let { "Habitaciones: $it" },
                                        perfil?.maxRoomies?.let { "Máx. roomies: $it" }
                                    )
                                )

                                ChipSection(
                                    title = "Reglas de convivencia",
                                    items = perfil?.reglasConvivencia
                                        ?.split(",")
                                        ?.map { it.trim() }
                                        ?: emptyList()
                                )

                                ChipSection(
                                    title = "Servicios incluidos",
                                    items = perfil?.serviciosIncluidos
                                        ?.split(",")
                                        ?.map { it.trim() }
                                        ?: emptyList()
                                )

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
}

/* ---------- Reutilizables ---------- */

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
    val filtered = items.filterNotNull().filter { it.isNotBlank() }
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
            .padding(top = 24.dp, bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Usamos IconButton para asegurar hit-target y respuesta correcta al click
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_atras_screens),
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }

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
                .clickable(role = Role.Button) { onBack() }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_aceptar),
            contentDescription = "Aceptar",
            modifier = Modifier
                .size(iconSize)
                .clickable(role = Role.Button) { onLike() }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_rechazar),
            contentDescription = "Rechazar",
            modifier = Modifier
                .size(iconSize)
                .clickable(role = Role.Button) { onReject() }
        )
    }
}

@Composable
fun NoMoreProfilesScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)   // Altura razonable, no bloquea botones
            .background(Color(0xFFD2D0D0)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay más perfiles 🥲",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}
