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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.profileconfig.presentation.TipoPerfil
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun Cuestionario3Screen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme

    val esBuscoLugar = state.tipoPerfil == TipoPerfil.BUSCO_LUGAR

    // ---- Variables locales ----
    var textPresupuesto by remember { mutableStateOf(state.presupuesto?.toString() ?: "") }
    var textArriendo by remember { mutableStateOf(state.arriendo?.toString() ?: "") }
    var tiempoEstancia by remember { mutableStateOf(state.tiempoEstancia ?: "") }

    // ---- Validación ----
    val formularioCompleto = if (esBuscoLugar) {
        textPresupuesto.isNotBlank() && tiempoEstancia.isNotBlank()
    } else {
        textArriendo.isNotBlank()
    }

    CuestionarioBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(current = 4)

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = if (esBuscoLugar) "PRESUPUESTO Y TIEMPO" else "DETALLES DE TU LUGAR",
                style = typography.displayLarge,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (esBuscoLugar)
                    "Indícanos cuánto puedes invertir y por cuánto tiempo necesitas quedarte."
                else
                    "Cuéntanos cuál es el arriendo mensual que ofreces para tu habitación o vivienda.",
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Campo principal (presupuesto o arriendo) ----
            QuestionWithIcon(
                icon = Icons.Default.AttachMoney,
                text = if (esBuscoLugar) "Presupuesto mensual aproximado" else "Arriendo mensual"
            )
            WhiteTextField(
                placeholder = if (esBuscoLugar) "Ejemplo: 800000" else "Ejemplo: 1200000",
                value = if (esBuscoLugar) textPresupuesto else textArriendo,
                onValueChange = {
                    if (esBuscoLugar) {
                        textPresupuesto = it.filter { ch -> ch.isDigit() || ch == '.' }
                        viewModel.actualizarPresupuesto(textPresupuesto.toDoubleOrNull())
                    } else {
                        textArriendo = it.filter { ch -> ch.isDigit() || ch == '.' }
                        viewModel.actualizarArriendo(textArriendo.toDoubleOrNull())
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Tiempo estimado (solo si busca casa) ----
            if (esBuscoLugar) {
                QuestionWithIcon(icon = Icons.Default.Schedule, text = "Tiempo estimado de estancia")
                Column(modifier = Modifier.fillMaxWidth()) {
                    listOf("Menos de 6 meses", "6-12 meses", "Más de 1 año").forEach { opcion ->
                        WhiteOutlinedButton(
                            text = opcion,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                tiempoEstancia = opcion
                                viewModel.actualizarTiempoEstancia(opcion)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ---- Botón siguiente ----
            Button(
                onClick = {
                    if (formularioCompleto) {
                        viewModel.avanzarPaso()
                        NavigationUtils.navigateToNextStep(
                            navController = navController,
                            tipoPerfil = state.tipoPerfil,
                            pasoActual = 4
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
fun PreviewCuestionario3() = RoomieMatchUTheme { Cuestionario3Screen() }
 */