package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

// ----------- Composable Principal -----------

@Composable
fun SubirFotosScreen(
    photoCount: Int = 1 // Cantidad de fotos ya añadidas
) {
    CuestionarioBackground {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Parte superior con contenido desplazable
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Paso 6
                ProgressDots(current = 6)

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "AGREGA MÁS FOTOS",
                    style = typography.displayLarge,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Agrega varias fotos para mostrar tu perfil completo.",
                    style = typography.bodyLarge,
                    color = colors.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ------- Contenedor de Fotos -------
                PhotosGrid(photoCount = photoCount)
            }

            // Botón siempre fijo abajo
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "SIGUIENTE",
                    style = typography.headlineMedium
                )
            }
        }
    }
}

// ----------- Grid de Fotos -----------

@Composable
fun PhotosGrid(photoCount: Int) {
    val colors = MaterialTheme.colorScheme
    val totalSlots = if (photoCount < 6) photoCount + 1 else 6 // máximo 6 con botón "+"
    val rows = if (photoCount <= 1) 1 else totalSlots / 3 + if (totalSlots % 3 != 0) 1 else 0

    Column(
        verticalArrangement = Arrangement.spacedBy(9.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var index = 0
        repeat(rows) {
            // Si hay solo una foto → layout horizontal de 2 columnas grandes
            if (photoCount <= 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PhotoFrame(hasImage = true, large = true, borderColor = colors.primary)
                    PhotoFrame(hasImage = false, large = true, borderColor = colors.primary)
                }
            } else {
                // Layout normal en grid de 3 columnas
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 3) {
                        if (index < totalSlots) {
                            if (index < photoCount) {
                                PhotoFrame(hasImage = true, large = false, borderColor = colors.primary)
                            } else {
                                PhotoFrame(hasImage = false, large = false, borderColor = colors.primary)
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
