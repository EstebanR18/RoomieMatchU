package com.example.approomiematchu.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.HighlightOff
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
import androidx.navigation.compose.rememberNavController
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
                .background(MaterialTheme.colorScheme.background)
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
                                R.drawable.imagen2
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
fun DescriptionTengoCasaScreen() {
    RoomieMatchUTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagen1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")

                Spacer(modifier = Modifier.height(8.dp))
                Text("Laura Sofía, 21", style = MaterialTheme.typography.headlineMedium)
                Text("Kennedy", style = MaterialTheme.typography.bodyLarge)

                SectionTitle("Estilo de vida")
                ChipRow(listOf("No fuma", "Pet Friendly", "Sin alergías"))

                SectionTitle("Presupuesto y tiempo de estancia")
                ChipRow(listOf("$600.000", "Menos de 6 meses"))

                SectionTitle("Intereses")
                ChipRow(listOf("Internet", "Amoblado", "Lavadora", "Baño Privado", "Parqueadero"))
            }
        }
    }
}

//* -------------------- BUSCO CASA - PANTALLA PRINCIPAL -------------------- */
@Composable
fun HomeBuscoCasaScreen(onDescriptionClick: () -> Unit) {
    RoomieMatchUTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                                R.drawable.imagen1,
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
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
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
                                        "No fumadores",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Pet Friendly",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                // 🔹 Imagen de perfil sobrepuesta (flotante)
                                Image(
                                    painter = painterResource(id = R.drawable.imagen2),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .offset(x = (-24).dp, y = (-60).dp) // 🔹 la desplaza para sobresalir del borde
                                        .clip(CircleShape)
                                        .border(
                                            4.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
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
fun DescriptionBuscoCasaScreen() {
    RoomieMatchUTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagen1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Pablo, 20", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.imagen2),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                }

                Text("Chapinero", style = MaterialTheme.typography.bodyLarge)

                SectionTitle("Estilo de vida")
                ChipRow(listOf("No fuma", "Pet Friendly"))

                SectionTitle("Presupuesto y tiempo de arrendamiento")
                ChipRow(listOf("$600.000", "Menos de 6 meses"))

                SectionTitle("Qué incluye el arriendo")
                ChipRow(listOf("Internet", "Amoblado", "Lavadora", "Baño Privado", "Agua Caliente"))

                SectionTitle("Reglas básicas de la casa")
                ChipRow(listOf("Se aceptan visitas", "Se aceptan mascotas", "Cada uno cocina"))
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



@Preview(showBackground = true)
@Composable
fun PreviewTengoCasaHome() {
    RoomieMatchUTheme {
        HomeTengoCasaScreen(onDescriptionClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTengoCasaDescripcion() {
    RoomieMatchUTheme {
        DescriptionTengoCasaScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBuscoCasaHome() {
    RoomieMatchUTheme {
        HomeBuscoCasaScreen(onDescriptionClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBuscoCasaDescripcion() {
    RoomieMatchUTheme {
        DescriptionBuscoCasaScreen()
    }
}

