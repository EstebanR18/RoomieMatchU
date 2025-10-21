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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.approomiematchu.R
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.theme.AppTypography
import com.example.approomiematchu.utils.RequestImagePermissions
import com.example.approomiematchu.utils.createTempImageFile
import com.example.approomiematchu.utils.rememberAdvancedImagePicker
import com.example.approomiematchu.utils.rememberImagePicker

@Composable
fun CuestionarioFotoPerfilScreen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val fotoLocal = state.fotoPerfilLocalUri
    var showDialog by remember { mutableStateOf(false) }
    var permisosConcedidos by remember { mutableStateOf(false) }

    // Solicitar permisos solo una vez
    RequestImagePermissions(
        onPermissionsGranted = { permisosConcedidos = true }
    )

    // Image picker (galería + cámara)
    val advancedPicker = rememberAdvancedImagePicker(
        onImageSelected = { uri ->
            uri?.let { viewModel.actualizarFotoPerfilLocal(it.toString()) }
        },
        onTakePhoto = { uri ->
            uri?.let { viewModel.actualizarFotoPerfilLocal(it.toString()) }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_profile),
            contentDescription = "Fondo Profile",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---- Progreso ----
            ProgressDots(total = 8, current = 6)

            // ---- Contenido central ----
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoLocal.isNullOrBlank()) {
                        // Icono inicial
                        Image(
                            painter = painterResource(id = R.drawable.ic_foto_perfil_cuestionario),
                            contentDescription = "Icono Foto Perfil",
                            modifier = Modifier
                                .size(250.dp)
                                .clickable {
                                    if (permisosConcedidos) showDialog = true
                                },
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        // Foto seleccionada
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
                                    width = 5.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .clickable {
                                    if (permisosConcedidos) showDialog = true
                                },
                            contentScale = ContentScale.Crop
                        )

                        // Cámara sobrepuesta (en esquina inferior derecha)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = (-16).dp, y = (-16).dp)
                                .zIndex(1f)
                                .size(60.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape
                                )
                                .clickable {
                                    if (permisosConcedidos) showDialog = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = "Cambiar foto",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // ---- Título y descripción ----
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "AGREGA TU FOTO DE PERFIL",
                        style = AppTypography.titulo1,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (fotoLocal.isNullOrBlank())
                            "Agrega tu foto para que los demás puedan reconocerte."
                        else
                            "Toca la foto o el ícono para cambiarla.",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ---- Botón inferior ----
            Button(
                onClick = {
                    if (fotoLocal.isNullOrBlank()) {
                        if (permisosConcedidos) showDialog = true
                    } else {
                        viewModel.avanzarPaso()
                        NavigationUtils.navigateToNextStep(
                            navController = navController,
                            tipoPerfil = state.tipoPerfil,
                            pasoActual = 6
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (fotoLocal.isNullOrBlank()) "AGREGA FOTO" else "SIGUIENTE",
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
                        }) { Text("Tomar foto") }

                        TextButton(onClick = {
                            showDialog = false
                            advancedPicker.launchGallery()
                        }) { Text("Elegir desde galería") }
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
