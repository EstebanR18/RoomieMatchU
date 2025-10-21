package com.example.approomiematchu.ui.profileconfig

import android.net.Uri
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.approomiematchu.R
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.utils.RequestImagePermissions
import com.example.approomiematchu.utils.createTempImageFile
import com.example.approomiematchu.utils.rememberAdvancedImagePicker
import com.example.approomiematchu.utils.rememberImagePicker

@Composable
fun CuestionarioFotoPerfilScreen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) } // diálogo de opciones cámara/galería
    var solicitarPermisos by remember { mutableStateOf(false) } // trigger para pedir permisos
    val fotoLocal = state.fotoPerfilLocalUri
    val puedeContinuar = !fotoLocal.isNullOrBlank()

    // Picker avanzado (cámara + galería)
    val advancedPicker = rememberAdvancedImagePicker(
        onImageSelected = { uri -> uri?.let { viewModel.actualizarFotoPerfilLocal(it.toString()) } },
        onTakePhoto = { uri -> uri?.let { viewModel.actualizarFotoPerfilLocal(it.toString()) } }
    )

    // 🔹 Ejecuta tu función de permisos solo cuando se requiera
    if (solicitarPermisos) {
        RequestImagePermissions(
            onPermissionsGranted = {
                solicitarPermisos = false
                showDialog = true
            }
        )
    }

    CuestionarioBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(total = 8, current = 6)

            // ---- Contenedor de foto ----
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape)
                        .clickable { solicitarPermisos = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (!fotoLocal.isNullOrBlank()) {
                        // Imagen seleccionada
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(fotoLocal)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto de perfil seleccionada",
                            modifier = Modifier
                                .matchParentSize()
                                .clip(CircleShape)
                                .border(
                                    width = 4.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                ),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_foto_perfil_cuestionario)
                        )

                        // Overlay + ícono cámara
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.15f))
                                .clip(CircleShape),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(48.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = "Cambiar foto",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    } else {
                        // Imagen por defecto
                        Image(
                            painter = painterResource(id = R.drawable.ic_foto_perfil_cuestionario),
                            contentDescription = "Agregar foto de perfil",
                            modifier = Modifier
                                .matchParentSize()
                                .border(
                                    width = 4.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    shape = CircleShape
                                ),
                            contentScale = ContentScale.Crop
                        )

                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Agregar foto",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "AGREGA TU FOTO DE PERFIL",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (fotoLocal != null)
                        "Toca la imagen para cambiarla"
                    else
                        "Toca el círculo para agregar tu foto",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // ---- Botón Siguiente ----
            Button(
                onClick = {
                    if (puedeContinuar) {
                        viewModel.avanzarPaso()
                        NavigationUtils.navigateToNextStep(
                            navController = navController,
                            tipoPerfil = state.tipoPerfil,
                            pasoActual = 6
                        )
                    }
                },
                enabled = puedeContinuar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = if (puedeContinuar) "CONTINUAR" else "AGREGA FOTO",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ---- Diálogo cámara o galería ----
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Selecciona una opción") },
                text = {
                    Column {
                        TextButton(onClick = {
                            showDialog = false
                            val outputUri = Uri.fromFile(createTempImageFile(context))
                            advancedPicker.launchCamera(outputUri)
                        }) {
                            Text("Tomar foto")
                        }
                        TextButton(onClick = {
                            showDialog = false
                            advancedPicker.launchGallery()
                        }) {
                            Text("Elegir desde galería")
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CuestionarioFotoPerfilScreenPreview() {
    RoomieMatchUTheme {
        CuestionarioFotoPerfilScreen()
    }
}
 */
