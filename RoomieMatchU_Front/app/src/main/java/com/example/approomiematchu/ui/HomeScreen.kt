package com.example.approomiematchu.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.approomiematchu.R
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun HomeScreen(navController: NavController) {

    // Puedes cambiar este estado manualmente para previsualizar dentro de la app
    var currentState by remember { mutableStateOf(1) }

    when (currentState) {
        1 -> HomeTengoCasaScreen(onDescriptionClick = { currentState = 2 })
        2 -> DescriptionTengoCasaScreen()
        3 -> HomeBuscoCasaScreen(onDescriptionClick = { currentState = 4 })
        4 -> DescriptionBuscoCasaScreen()
    }
}

/* -------------------- COMPONENTES REUTILIZABLES -------------------- */

@Composable
fun TopBar() {
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
            modifier = Modifier.size(35.dp)
        )
    }
}

@Composable
fun ImageCarousel(imageList: List<Int>, modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableStateOf(0) }

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = imageList[selectedIndex]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clickable {
                    selectedIndex = (selectedIndex + 1) % imageList.size
                },
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        ) {
            imageList.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .height(4.dp)
                        .width(40.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (index == selectedIndex)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surface
                        )
                )
            }
        }
    }
}
/* -------------------- TENGO CASA - PANTALLA PRINCIPAL -------------------- */
@Composable
fun HomeTengoCasaScreen(onDescriptionClick: () -> Unit) {
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
                verticalArrangement = Arrangement.Center // 🔹 CENTRA verticalmente todo
            ) {
                TopBar()

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
                        // Carrusel ocupa todo el recuadro
                        ImageCarousel(
                            imageList = listOf(
                                R.drawable.imagen1,
                                R.drawable.imagen2,
                                R.drawable.imagen3
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        )

                        // Información debajo del carrusel
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(16.dp)
                                .clickable { onDescriptionClick() }
                        ) {
                            Text(
                                "Laura Sofía, 21",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Kennedy",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Habitación individual",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 🔹 Botones inferiores dentro de la columna, centrados abajo
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
                            .clickable { /* acción atrás */ }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_aceptar),
                        contentDescription = "Aceptar",
                        modifier = Modifier
                            .size(iconSize)
                            .clickable { /* acción aceptar */ }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_rechazar),
                        contentDescription = "Rechazar",
                        modifier = Modifier
                            .size(iconSize)
                            .clickable { /* acción rechazar */ }
                    )
                }
            }
        }
    }
}


/* -------------------- TENGO CASA - DESCRIPCIÓN -------------------- */
@Composable
fun DescriptionTengoCasaScreen(onBackClick: () -> Unit = {}) {
    RoomieMatchUTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {
            // 🔹 Encabezado fijo superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD2D0D0))
                    .padding(top = 24.dp, bottom = 12.dp)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center
            ) {
                // Ícono de volver
                Image(
                    painter = painterResource(id = R.drawable.ic_atras_screens),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() }
                        .padding(start = 16.dp)
                )

                // Texto centrado realmente
                Text(
                    text = "ROOMIE\nMATCH U",
                    style = MaterialTheme.typography.displayLarge.copy(lineHeight = 21.sp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 🔹 Contenido con scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 100.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val imageHeight = (screenHeight * 0.45f).coerceIn(300.dp, 500.dp)

                // 🔹 Contenedor principal con bordes redondeados
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ImageCarousel(
                            imageList = listOf(
                                R.drawable.imagen1
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight * 0.85f) // 🔽 más bajo que el home
                                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 350.dp)
                                .verticalScroll(rememberScrollState())
                                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp))
                                .padding(20.dp)
                        ) {
                            Text(
                                "Laura Sofía, 21",
                                style = MaterialTheme.typography.displayLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                "Chapinero",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Estilo de vida
                            SectionHeader("Estilo de vida")
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                listOf(
                                    "No fumo",
                                    "Estoy dispuesto a vivir con mascotas",
                                    "Sin alergias",
                                    "Tengo mascotas"
                                ).forEach { Chip(it) }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Precio dispuesto a pagar
                            SectionHeader("Precio dispuesto a pagar")
                            Chip("$600.000")

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Número de personas con las que estarías dispuesto a vivir
                            SectionHeader("Número de personas con las que estarías dispuesto a vivir")
                            Chip("3")

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Servicios indispensables que buscas
                            SectionHeader("Servicios indispensables que buscas")
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                listOf(
                                    "Internet",
                                    "Amoblado",
                                    "Lavadora",
                                    "Baño Privado",
                                    "Agua Caliente",
                                    "Secadora"
                                ).forEach { Chip(it) }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Tipo de habitación
                            SectionHeader("Tipo de habitación")
                            Chip("Individual")

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Fecha en la que necesitas mudarte
                            SectionHeader("Fecha en la que necesitas mudarte")
                            Chip("Inmediato")

                        }
                    }
                }
            }
        }
    }
}

/* ---------- Reutilizables ---------- */

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


//* -------------------- BUSCO CASA - PANTALLA PRINCIPAL -------------------- */
@Composable
fun HomeBuscoCasaScreen(onDescriptionClick: () -> Unit) {
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
                TopBar()

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
                        // Carrusel ocupa todo el recuadro
                        ImageCarousel(
                            imageList = listOf(
                                R.drawable.imagen3,
                                R.drawable.imagen2
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        )

                        // 🔹 Información debajo del carrusel + foto de perfil
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
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
                                        "Pablo, 20",
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Chapinero",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Habitación individual",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Image(
                                    painter = painterResource(id = R.drawable.imagen2),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .size(90.dp)
                                        .clip(CircleShape)
                                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 🔹 Botones inferiores dentro de la columna, centrados abajo
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
                            .clickable { /* acción atrás */ }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_aceptar),
                        contentDescription = "Aceptar",
                        modifier = Modifier
                            .size(iconSize)
                            .clickable { /* acción aceptar */ }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_rechazar),
                        contentDescription = "Rechazar",
                        modifier = Modifier
                            .size(iconSize)
                            .clickable { /* acción rechazar */ }
                    )
                }
            }
        }
    }
}


/* -------------------- BUSCO CASA - DESCRIPCIÓN -------------------- */
@Composable
fun DescriptionBuscoCasaScreen(onBackClick: () -> Unit = {}) {
    RoomieMatchUTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD2D0D0))
        ) {
            // 🔹 Encabezado fijo superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD2D0D0))
                    .padding(top = 24.dp, bottom = 12.dp)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center
            ) {
                // Ícono de volver
                Image(
                    painter = painterResource(id = R.drawable.ic_atras_screens),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() }
                        .padding(start = 16.dp)
                )

                // Texto centrado realmente
                Text(
                    text = "ROOMIE\nMATCH U",
                    style = MaterialTheme.typography.displayLarge.copy(lineHeight = 21.sp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 🔹 Contenido con scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 100.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val imageHeight = (screenHeight * 0.45f).coerceIn(300.dp, 500.dp)

                // 🔹 Contenedor principal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // 🔹 Carrusel de imágenes
                        ImageCarousel(
                            imageList = listOf(
                                R.drawable.imagen3,
                                R.drawable.imagen4
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight * 0.85f)
                                .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        )

                        // 🔹 Contenido con scroll interno
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 350.dp)
                                .verticalScroll(rememberScrollState())
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)
                                )
                                .padding(20.dp)
                        ) {
                            // 🔹 Nombre + foto de perfil
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.imagen2),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    "Pablo, 20",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                "Chapinero",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Estilo de vida
                            SectionHeader("Estilo de vida")
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                listOf(
                                    "No fumo",
                                    "Estoy dispuesto a vivir con mascotas",
                                    "Sin alergias",
                                    "Tengo mascotas"
                                ).forEach { Chip(it) }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Precio dispuesto a pagar
                            SectionHeader("Precio dispuesto a pagar")
                            Chip("$600.000")

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Número de personas con las que vivir
                            SectionHeader("Número de personas con las que estarías dispuesto a vivir")
                            Chip("3")

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Servicios indispensables
                            SectionHeader("Servicios indispensables que buscas")
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                listOf(
                                    "Internet",
                                    "Amoblado",
                                    "Lavadora",
                                    "Baño Privado",
                                    "Agua Caliente",
                                    "Secadora"
                                ).forEach { Chip(it) }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Tipo de habitación
                            SectionHeader("Tipo de habitación")
                            Chip("Individual")

                            Spacer(modifier = Modifier.height(20.dp))

                            // 🔹 Fecha para mudarte
                            SectionHeader("Fecha en la que necesitas mudarte")
                            Chip("Inmediato")
                        }
                    }
                }
            }
        }
    }
}

/* -------------------- COMPONENTES DE APOYO -------------------- */

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

@Composable
fun ChipRow(items: List<String>) {
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
