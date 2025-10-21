package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun Cuestionario2Screen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var fuma by remember { mutableStateOf<Boolean?>(state.fuma) }
    var alergico by remember { mutableStateOf<Boolean?>(state.alergico) }
    var detalleAlergia by remember { mutableStateOf(state.detalleAlergia ?: "") }
    var mascota by remember { mutableStateOf<Boolean?>(state.mascota) }
    var dispuestoMascotas by remember { mutableStateOf<Boolean?>(null) }

    val formularioCompleto =
        fuma != null && alergico != null && mascota != null && dispuestoMascotas != null &&
                (!alergico!! || detalleAlergia.isNotBlank())

    CuestionarioBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(current = 3)

            Spacer(modifier = Modifier.height(30.dp))
            Text("TU ESTILO DE VIDA", style = typography.displayLarge, color = colors.onBackground)
            Text(
                "Queremos conocer tus hábitos para encontrar la mejor compatibilidad.",
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---- ¿Fumas? ----
            QuestionWithIcon(icon = Icons.Default.SmokingRooms, text = "¿Fumas?")
            YesNoButtons(
                onYes = { fuma = true; viewModel.actualizarFuma(true) },
                onNo = { fuma = false; viewModel.actualizarFuma(false) }
            )

            // ---- ¿Eres alérgico? ----
            QuestionWithIcon(icon = Icons.Default.Healing, text = "¿Eres alérgico a algo?")
            YesNoButtons(
                onYes = { alergico = true; viewModel.actualizarAlergico(true, detalleAlergia) },
                onNo = { alergico = false; detalleAlergia = ""; viewModel.actualizarAlergico(false, null) }
            )

            if (alergico == true) {
                WhiteTextField(
                    placeholder = "¿A qué?",
                    value = detalleAlergia,
                    onValueChange = {
                        detalleAlergia = it
                        viewModel.actualizarAlergico(true, it)
                    }
                )
            }

            // ---- ¿Tienes mascotas? ----
            QuestionWithIcon(icon = Icons.Default.Pets, text = "¿Tienes mascotas?")
            YesNoButtons(
                onYes = { mascota = true; viewModel.actualizarMascota(true) },
                onNo = { mascota = false; viewModel.actualizarMascota(false) }
            )

            // ---- ¿Dispuesto a vivir con mascotas? ----
            QuestionWithIcon(icon = Icons.Default.Home, text = "¿Estás dispuesto a vivir con mascotas?")
            YesNoButtons(
                onYes = { dispuestoMascotas = true },
                onNo = { dispuestoMascotas = false }
            )

            Spacer(modifier = Modifier.weight(1f))

            // ---- Botón siguiente ----
            Button(
                onClick = {
                    if (formularioCompleto) {
                        viewModel.avanzarPaso()
                        NavigationUtils.navigateToNextStep(
                            navController = navController,
                            tipoPerfil = state.tipoPerfil,
                            pasoActual = 3
                        )
                    }
                },
                enabled = formularioCompleto,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
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

/*
@Preview(showBackground = true)
@Composable
fun PreviewCuestionario2() = RoomieMatchUTheme { Cuestionario2Screen() }
 */