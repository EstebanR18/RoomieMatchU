package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
fun CuestionarioBuscoCasaScreen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Estados locales para las selecciones
    var barrioSeleccionado by remember { mutableStateOf(state.barrio ?: "") }
    var personasConvivenciaSeleccionada by remember { mutableStateOf(state.personasConvivencia ?: "") }
    var tipoHabitacionSeleccionada by remember { mutableStateOf(state.tipoHabitacion ?: "") }
    var serviciosSeleccionados by remember { mutableStateOf(state.serviciosDeseados ?: "") }
    var fechaMudanzaSeleccionada by remember { mutableStateOf(state.fechaMudanza ?: "") }

    // Validación del formulario
    val formularioCompleto = barrioSeleccionado.isNotBlank() &&
            personasConvivenciaSeleccionada.isNotBlank() &&
            tipoHabitacionSeleccionada.isNotBlank() &&
            serviciosSeleccionados.isNotBlank() &&
            fechaMudanzaSeleccionada.isNotBlank()

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
                "DETALLES DE BÚSQUEDA",
                style = typography.displayLarge,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                "Cuéntanos qué tipo de lugar estás buscando",
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable form
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Zona o barrio preferido
                QuestionWithIcon(icon = Icons.Default.Home, text = "Zona o barrio preferido")
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

                // Número de personas
                QuestionWithIcon(icon = Icons.Default.Person, text = "Número de personas con las que estarías dispuesto a vivir")
                Column(modifier = Modifier.fillMaxWidth()) {
                    listOf("Menos de 3", "3-5 personas", "Más de 5").forEach { opcion ->
                        WhiteOutlinedButton(
                            text = opcion,
                            modifier = Modifier.fillMaxWidth(),
                            isSelected = personasConvivenciaSeleccionada == opcion,
                            onClick = {
                                personasConvivenciaSeleccionada = opcion
                                viewModel.actualizarPersonasConvivencia(opcion)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tipo de habitación
                QuestionWithIcon(icon = Icons.Default.Bed, text = "Tipo de habitación buscada")
                Column(modifier = Modifier.fillMaxWidth()) {
                    listOf("Individual", "Compartida").forEach { opcion ->
                        WhiteOutlinedButton(
                            text = opcion,
                            modifier = Modifier.fillMaxWidth(),
                            isSelected = tipoHabitacionSeleccionada == opcion,
                            onClick = {
                                tipoHabitacionSeleccionada = opcion
                                viewModel.actualizarTipoHabitacion(opcion)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Servicios indispensables
                QuestionWithIcon(icon = Icons.Default.Home, text = "Servicios indispensables (Selecciona varias opciones)")
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
                                        viewModel.actualizarServiciosDeseados(serviciosTexto)
                                    }
                                )
                            }
                            // Si hay solo un servicio en la fila, añadir un espacio vacío
                            if (rowServicios.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fecha de mudanza
                QuestionWithIcon(icon = Icons.Default.CalendarMonth, text = "Fecha en la que necesitas mudarte")
                Column(modifier = Modifier.fillMaxWidth()) {
                    listOf("Inmediato", "Próximo mes", "En 2 a 3 meses").forEach { opcion ->
                        WhiteOutlinedButton(
                            text = opcion,
                            modifier = Modifier.fillMaxWidth(),
                            isSelected = fechaMudanzaSeleccionada == opcion,
                            onClick = {
                                fechaMudanzaSeleccionada = opcion
                                viewModel.actualizarFechaMudanza(opcion)
                            }
                        )
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
                            tipoPerfil = TipoPerfil.BUSCO_LUGAR,
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
fun PreviewCuestionarioBuscoCasa() = RoomieMatchUTheme { CuestionarioBuscoCasaScreen() }
 */
