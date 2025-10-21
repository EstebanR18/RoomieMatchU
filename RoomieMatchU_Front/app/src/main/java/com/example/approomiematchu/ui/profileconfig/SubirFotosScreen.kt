package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.profileconfig.presentation.TipoPerfil
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme
import com.example.approomiematchu.utils.rememberImagePicker

// ----------- Composable Principal -----------
@Composable
fun SubirFotosScreen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val imagePicker = rememberImagePicker { uri ->
        uri?.let {
            if (state.fotosResidencia.size < 6) {
                viewModel.agregarFotoResidenciaLocal(it.toString())
            }
        }
    }

    CuestionarioBackground {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ----- Parte superior -----
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProgressDots(total = 8, current = 8)
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "AGREGA MÁS FOTOS",
                    style = typography.displayLarge,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Agrega varias fotos para mostrar tu residencia completa.",
                    style = typography.bodyLarge,
                    color = colors.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ------- Contenedor de Fotos -------
                PhotosGrid(
                    photos = state.fotosResidencia,
                    onAddPhoto = {
                        if (state.fotosResidencia.size < 6) imagePicker.launch()
                    }
                )
            }

            // ----- Botón inferior -----
            Button(
                onClick = {
                    viewModel.avanzarPaso()
                    NavigationUtils.navigateToNextStep(
                        navController = navController,
                        tipoPerfil = TipoPerfil.TENGO_LUGAR,
                        pasoActual = 8
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("SIGUIENTE", style = typography.headlineMedium)
            }
        }
    }
}


// ----------- Grid de Fotos -----------
@Composable
fun PhotosGrid(
    photos: List<String>,
    onAddPhoto: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val photoCount = photos.size
    val totalSlots = if (photoCount < 6) photoCount + 1 else 6
    val rows = if (photoCount <= 1) 1 else totalSlots / 3 + if (totalSlots % 3 != 0) 1 else 0
    var index = 0

    Column(
        verticalArrangement = Arrangement.spacedBy(9.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(rows) {
            if (photoCount <= 1) {
                // ------- Layout de 2 columnas grandes -------
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (photoCount > 0) {
                        AsyncImage(
                            model = photos.first(),
                            contentDescription = "Foto residencia",
                            modifier = Modifier
                                .size(160.dp, 260.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(3.dp, colors.primary, RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        PhotoFrame(hasImage = false, large = true, borderColor = colors.primary)
                    }

                    // Botón para añadir
                    Box(
                        modifier = Modifier
                            .size(160.dp, 260.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(3.dp, colors.primary, RoundedCornerShape(16.dp))
                            .clickable { onAddPhoto() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddCircleOutline,
                            contentDescription = "Agregar foto",
                            tint = colors.primary,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            } else {
                // ------- Grid normal 3 columnas -------
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 3) {
                        if (index < totalSlots) {
                            if (index < photoCount) {
                                AsyncImage(
                                    model = photos[index],
                                    contentDescription = "Foto residencia",
                                    modifier = Modifier
                                        .size(110.dp, 170.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(3.dp, colors.primary, RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                // Botón para añadir
                                Box(
                                    modifier = Modifier
                                        .size(110.dp, 170.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(3.dp, colors.primary, RoundedCornerShape(16.dp))
                                        .clickable { onAddPhoto() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AddCircleOutline,
                                        contentDescription = "Agregar foto",
                                        tint = colors.primary,
                                        modifier = Modifier.size(40.dp)
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
}


// ----------- Marco de Foto Individual -----------
@Composable
fun PhotoFrame(hasImage: Boolean, large: Boolean, borderColor: Color) {
    val frameWidth = if (large) 160.dp else 110.dp
    val frameHeight = if (large) 260.dp else 170.dp

    Box(
        modifier = Modifier
            .size(frameWidth, frameHeight)
            .background(
                color = if (hasImage) Color.LightGray.copy(alpha = 0.7f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(3.dp, borderColor),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!hasImage) {
            Icon(
                imageVector = Icons.Outlined.AddCircleOutline,
                contentDescription = "Añadir foto",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(if (large) 60.dp else 35.dp)
            )
        }
    }
}


// ----------- Previews -----------
/*
@Preview(showBackground = true)
@Composable
fun PreviewSubirFotos1() = RoomieMatchUTheme { SubirFotosScreen(photoCount = 1) }

@Preview(showBackground = true)
@Composable
fun PreviewSubirFotos2() = RoomieMatchUTheme { SubirFotosScreen(photoCount = 2) }

@Preview(showBackground = true)
@Composable
fun PreviewSubirFotos3() = RoomieMatchUTheme { SubirFotosScreen(photoCount = 3) }

@Preview(showBackground = true)
@Composable
fun PreviewSubirFotos4() = RoomieMatchUTheme { SubirFotosScreen(photoCount = 4) }
 */
