package com.example.approomiematchu.ui.profileconfig

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.approomiematchu.R
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.profileconfig.presentation.TipoPerfil
import com.example.approomiematchu.ui.theme.AppTypography
import com.example.approomiematchu.utils.uriToFile
import kotlinx.coroutines.launch

@Composable
fun CuestionarioCompletadoScreen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Imagen de fondo
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
            // Imagen superior
            Image(
                painter = painterResource(id = R.drawable.profile_icon),
                contentDescription = "Roomie Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp)
            )

            // Títulos
            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "FELICIDADES",
                    style = AppTypography.titulo2,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "YA COMPLETASTE",
                    style = AppTypography.titulo1,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "TU PERFIL",
                    style = AppTypography.titulo1,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            // ---- Botón SIGUIENTE ----
            Button(
                onClick = {
                    scope.launch {
                        enviarFormularioCompleto(context, viewModel, navController)
                    }
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!state.isLoading)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("ENVIANDO...", style = MaterialTheme.typography.headlineMedium)
                } else {
                    Text(
                        "SIGUIENTE",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Indicador flotante (overlay)
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Subiendo información...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

private fun enviarFormularioCompleto(
    context: Context,
    viewModel: PerfilCuestionarioViewModel,
    navController: NavController
) {
    val state = viewModel.state.value

    Log.d(
        "PerfilEnvio", """
        🚀 Iniciando formulario completo
        User ID: ${state.userId}
        Tipo perfil: ${state.tipoPerfil}
        Foto local URI: ${state.fotoPerfilLocalUri}
        Fotos residencia: ${state.fotosResidencia.size}
    """.trimIndent()
    )

    // --- 1️⃣ Crear el perfil primero ---
    when (state.tipoPerfil) {
        TipoPerfil.TENGO_LUGAR -> {
            Log.d("PerfilEnvio", "📦 Creando perfil TENGO_LUGAR...")

            viewModel.enviarPerfilTengo(
                onSuccess = {
                    Log.d("PerfilEnvio", "✅ Perfil TENGO_LUGAR creado correctamente.")

                    // --- 2️⃣ Subir la foto de perfil ---
                    subirFotoPerfilYResidencia(context, viewModel, navController)
                },
                onError = { error ->
                    Log.e("PerfilEnvio", "❌ Error al crear perfil TENGO_LUGAR: $error")
                    Toast.makeText(context, "Error al enviar perfil: $error", Toast.LENGTH_LONG).show()
                }
            )
        }

        TipoPerfil.BUSCO_LUGAR -> {
            Log.d("PerfilEnvio", "📦 Creando perfil BUSCO_LUGAR...")

            viewModel.enviarPerfilBusco(
                onSuccess = {
                    Log.d("PerfilEnvio", "✅ Perfil BUSCO_LUGAR creado correctamente.")

                    // --- 2️⃣ Subir la foto de perfil ---
                    subirFotoPerfilYResidencia(context, viewModel, navController)
                },
                onError = { error ->
                    Log.e("PerfilEnvio", "❌ Error al crear perfil BUSCO_LUGAR: $error")
                    Toast.makeText(context, "Error al enviar perfil: $error", Toast.LENGTH_LONG).show()
                }
            )
        }

        else -> {
            Log.e("PerfilEnvio", "⚠️ Tipo de perfil no válido o nulo: ${state.tipoPerfil}")
            Toast.makeText(context, "Tipo de perfil no válido", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun subirFotoPerfilYResidencia(
    context: Context,
    viewModel: PerfilCuestionarioViewModel,
    navController: NavController
) {
    val state = viewModel.state.value

    viewModel.subirFotoPerfilAlFinal(
        context = context,
        onSuccess = { fotoUrl ->
            Log.d("PerfilEnvio", "✅ Foto de perfil subida correctamente: $fotoUrl")

            val localFiles = state.fotosResidencia.mapNotNull { uri ->
                uriToFile(context, uri)
            }

            if (localFiles.isNotEmpty()) {
                Log.d("PerfilEnvio", "📸 Subiendo ${localFiles.size} fotos de residencia...")

                viewModel.subirFotosResidencia(
                    files = localFiles,
                    onSuccess = { urls ->
                        Log.d("PerfilEnvio", "✅ Fotos de residencia subidas: $urls")
                        finalizarEnvio(context, navController)
                    },
                    onError = { error ->
                        Log.e("PerfilEnvio", "❌ Error al subir fotos de residencia: $error")
                        Toast.makeText(
                            context,
                            "Error al subir fotos: ${error.take(2000)}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            } else {
                Log.d("PerfilEnvio", "ℹ️ No hay fotos de residencia, finalizando envío.")
                finalizarEnvio(context, navController)
            }
        },
        onError = { error ->
            Log.e(
                "PerfilEnvio", """
                ❌ Error al subir foto de perfil
                Mensaje: $error
                Foto local URI: ${state.fotoPerfilLocalUri}
                User ID: ${state.userId}
            """.trimIndent()
            )

            Toast.makeText(
                context,
                "Error al subir foto de perfil: ${error.take(2000)}",
                Toast.LENGTH_LONG
            ).show()
        }
    )
}

private fun finalizarEnvio(
    context: Context,
    navController: NavController
) {
    Toast.makeText(context, "Perfil enviado correctamente", Toast.LENGTH_SHORT).show()
    Log.d("PerfilEnvio", "🎉 Envío completo: perfil + fotos + foto de perfil.")
    NavigationUtils.navigateAndClear(navController, AppScreens.HomeScreen.route)
}


/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CuestionarioCompletadoScreenPreview() {
    RoomieMatchUTheme {
        CuestionarioCompletadoScreen()
    }
}
 */
