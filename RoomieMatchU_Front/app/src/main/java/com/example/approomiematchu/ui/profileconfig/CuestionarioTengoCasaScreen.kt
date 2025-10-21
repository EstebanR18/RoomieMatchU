package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
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
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.profileconfig.presentation.TipoPerfil
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun CuestionarioTengoCasaScreen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Estados locales para las selecciones
    var barrioSeleccionado by remember { mutableStateOf(state.barrio ?: "") }
    var cantidadHabitaciones by remember { mutableStateOf(state.cantidadHabitaciones?.toString() ?: "") }
    var maxRoomies by remember { mutableStateOf("") }
    var serviciosSeleccionados by remember { mutableStateOf(state.serviciosIncluidos ?: "") }
    var reglasSeleccionadas by remember { mutableStateOf(state.reglasConvivencia ?: "") }

    // Validación del formulario
    val formularioCompleto = barrioSeleccionado.isNotBlank() &&
            cantidadHabitaciones.isNotBlank() &&
            maxRoomies.isNotBlank() &&
            serviciosSeleccionados.isNotBlank() &&
            reglasSeleccionadas.isNotBlank()

    CuestionarioBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Paso 5
            ProgressDots(total = 8, current = 5)

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "DETALLES DE TU VIVIENDA",
                style = typography.displayLarge,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                "Cuéntanos sobre el lugar que ofreces",
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Zona o barrio de residencia
                QuestionWithIcon(icon = Icons.Default.Home, text = "Zona o barrio de residencia")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Usaquén", "Chapinero", "Santa Fe", "San Cristóbal", "Usme",
                        "Tunjuelito", "Bosa", "Kennedy", "Fontibón", "Engativá",
                        "Suba", "Barrios Unidos", "Teusaquillo", "Los Mártires",
                        "Antonio Nariño", "Puente Aranda", "La Candelaria",
                        "Rafael Uribe Uribe", "Ciudad Bolívar", "Sumapaz"
                    ).forEach { barrio ->
                        WhiteOutlinedButton(
                            text = barrio,
                            modifier = Modifier,
                            isSelected = barrioSeleccionado == barrio,
                            onClick = {
                                barrioSeleccionado = barrio
                                viewModel.actualizarBarrio(barrio)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Número de habitaciones
                QuestionWithIcon(icon = Icons.Default.Bed, text = "Número de habitaciones disponibles")
                WhiteTextField(
                    placeholder = "Ejemplo: 3",
                    value = cantidadHabitaciones,
                    onValueChange = {
                        cantidadHabitaciones = it.filter { ch -> ch.isDigit() }
                        viewModel.actualizarCantidadHabitaciones(cantidadHabitaciones.toIntOrNull() ?: 0)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Máximo de roomies
                QuestionWithIcon(icon = Icons.Default.Bed, text = "Máximo número de roomies permitidos")
                WhiteTextField(
                    placeholder = "Ejemplo: 2",
                    value = maxRoomies,
                    onValueChange = {
                        maxRoomies = it.filter { ch -> ch.isDigit() }
                        viewModel.actualizarMaxRoomies(maxRoomies.toIntOrNull() ?: 1)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Servicios incluidos
                QuestionWithIcon(icon = Icons.Default.Home, text = "Servicios incluidos (Selecciona varias opciones)")
                val servicios = listOf(
                    "Internet", "Agua Caliente", "Lavadora",
                    "Secadora", "Amoblado básico", "Baño privado",
                    "Televisión", "Cocina equipada", "Nevera compartida",
                    "Parqueadero", "Espacios comunes", "Acceso inclusivo"
                )

                var serviciosList by remember {
                    mutableStateOf(serviciosSeleccionados.split(",").map { it.trim() }.filter { it.isNotBlank() }.toMutableSet())
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    servicios.chunked(2).forEach { rowServicios ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowServicios.forEach { servicio ->
                                WhiteOutlinedButton(
                                    text = servicio,
                                    modifier = Modifier.weight(1f),
                                    isSelected = serviciosList.contains(servicio),
                                    onClick = {
                                        if (serviciosList.contains(servicio)) {
                                            serviciosList.remove(servicio)
                                        } else {
                                            serviciosList.add(servicio)
                                        }
                                        val serviciosTexto = serviciosList.joinToString(", ")
                                        serviciosSeleccionados = serviciosTexto
                                        viewModel.actualizarServiciosIncluidos(serviciosTexto)
                                    }
                                )
                            }
                            if (rowServicios.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reglas básicas de la casa
                QuestionWithIcon(icon = Icons.Default.Warning, text = "Reglas básicas de la casa (Selecciona varias opciones)")
                val reglas = listOf(
                    "Visitas limitadas", "Horarios establecidos", "No fiestas",
                    "Limpieza semanal", "Se aceptan mascotas", "Cocina compartida",
                    "Respeto al ruido", "No fumar", "Compartir gastos"
                )

                var reglasList by remember {
                    mutableStateOf(reglasSeleccionadas.split(",").map { it.trim() }.filter { it.isNotBlank() }.toMutableSet())
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    reglas.chunked(2).forEach { rowReglas ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowReglas.forEach { regla ->
                                WhiteOutlinedButton(
                                    text = regla,
                                    modifier = Modifier.weight(1f),
                                    isSelected = reglasList.contains(regla),
                                    onClick = {
                                        if (reglasList.contains(regla)) {
                                            reglasList.remove(regla)
                                        } else {
                                            reglasList.add(regla)
                                        }
                                        val reglasTexto = reglasList.joinToString(", ")
                                        reglasSeleccionadas = reglasTexto
                                        viewModel.actualizarReglasConvivencia(reglasTexto)
                                    }
                                )
                            }
                            if (rowReglas.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (formularioCompleto) {
                        viewModel.avanzarPaso()
                        NavigationUtils.navigateToNextStep(
                            navController = navController,
                            tipoPerfil = TipoPerfil.TENGO_LUGAR,
                            pasoActual = 5
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
fun PreviewCuestionarioTengoCasaScreen() = RoomieMatchUTheme { CuestionarioTengoCasaScreen() }
 */
