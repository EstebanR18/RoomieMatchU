package com.example.approomiematchu.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
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
import com.example.approomiematchu.R
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilScreen() {
    val scrollState = rememberScrollState()

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
                Image(
                    painter = painterResource(id = R.drawable.imagen1),
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                "Laura Sofía, 21",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Soy Laura y busco apartamento en Chapinero. Me gusta un ambiente tranquilo, ordenado y con buena convivencia.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Chapinero
            Text(
                "Chapinero",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Estilo de vida
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
                    "No fumo",
                    "Estoy dispuesto a vivir con mascotas",
                    "Sin alergias",
                    "Tengo mascotas"
                ).forEach { Chip(it) }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Precio
            Text(
                "Precio dispuesto a pagar",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Chip("$600.000")

            Spacer(modifier = Modifier.height(20.dp))

            // Personas
            Text(
                "Número de personas con las que estarías dispuesto a vivir",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Chip("3")

            Spacer(modifier = Modifier.height(20.dp))

            // Servicios
            Text(
                "Servicios indispensables que buscas",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    "Internet", "Amoblado", "Lavadora",
                    "Baño Privado", "Agua Caliente", "Secadora"
                ).forEach { Chip(it) }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tipo habitación
            Text(
                "Tipo de habitación",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Chip("Individual")

            Spacer(modifier = Modifier.height(20.dp))

            // Fecha mudanza
            Text(
                "Fecha en la que necesitas mudarte",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Chip("Inmediato")

            Spacer(modifier = Modifier.height(100.dp)) // espacio final visible
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilEditarScreen() {
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
                    painter = painterResource(id = R.drawable.imagen1),
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
                "Laura Sofía, 21",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción editable con estilo de Card
            var isEditing by remember { mutableStateOf(false) }
            var descripcion by remember { mutableStateOf("Soy Laura y busco apartamento en Chapinero. Me gusta un ambiente tranquilo, ordenado y con buena convivencia.") }

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
                                colors = customTextFieldColors(
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
                "Precio dispuesto a pagar",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            TextField(
                value = precio,
                onValueChange = { precio = it },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = customTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Personas editable
            Text(
                "Número de personas con las que estarías dispuesto a vivir",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            TextField(
                value = personas,
                onValueChange = { personas = it },
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

            // Servicios indispensables
            Text(
                "Servicios indispensables que buscas",
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

            // Tipo de habitación
            Text(
                "Tipo de habitación",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Individual", "Compartida").forEach { Chip(it) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha de mudanza
            Text(
                "Fecha en la que necesitas mudarte",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Inmediato", "Próximo mes", "De 2 a 3 meses").forEach { Chip(it) }
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


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=800dp,dpi=420")
@Composable
fun PerfilScreenPreview() {
    RoomieMatchUTheme {
        PerfilScreen()
    }
}


@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=800dp,dpi=420")
@Composable
fun PerfilEditarPreview() {
    RoomieMatchUTheme {
        PerfilEditarScreen()
    }
}
