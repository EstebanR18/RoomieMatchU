package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme
import java.util.Calendar

@Composable
fun Cuestionario1Screen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var nombre by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var generoSeleccionado by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    CuestionarioBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(current = 2)

            Spacer(modifier = Modifier.height(30.dp))
            Text("IDENTIFÍCATE", style = typography.displayLarge, color = colors.onBackground)
            Text(
                "Cuéntanos quién eres para que podamos crear tu perfil.",
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = colors.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ---- Nombre ----
            QuestionWithIcon(icon = Icons.Default.Person, text = "Nombre de usuario")
            WhiteTextField(
                placeholder = "Escribe tu nombre",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Cumpleaños ----
            QuestionWithIcon(icon = Icons.Default.CalendarMonth, text = "Cumpleaños")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(
                    Triple("DD", dia) { v: String -> dia = v.filter { it.isDigit() }.take(2) },
                    Triple("MM", mes) { v: String -> mes = v.filter { it.isDigit() }.take(2) },
                    Triple("AA", anio) { v: String -> anio = v.filter { it.isDigit() }.take(4) }
                ).forEach { (ph, value, update) ->
                    WhiteTextField(
                        placeholder = ph,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Género ----
            QuestionWithIcon(icon = Icons.Default.Wc, text = "Género")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("Femenino", "Masculino", "Otro").forEach {
                    WhiteOutlinedButton(
                        text = it,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            generoSeleccionado = it
                            viewModel.actualizarGenero(it)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ---- Botón de siguiente ----
            Button(
                onClick = {
                    val diaNum = dia.toIntOrNull()
                    val mesNum = mes.toIntOrNull()
                    val anioNum = anio.toIntOrNull()

                    if (diaNum == null || mesNum == null || anioNum == null) {
                        dialogMessage = "Por favor completa tu fecha de nacimiento."
                        showDialog = true
                    } else if (!esMayorDeEdad(diaNum, mesNum, anioNum)) {
                        dialogMessage = "Debes ser mayor de 18 años para continuar."
                        showDialog = true
                    } else {
                        // Guardar datos y avanzar
                        viewModel.actualizarCampoFechaNacimiento("$anioNum-$mesNum-$diaNum")
                        NavigationUtils.navigateToScreen(navController, AppScreens.Cuestionario2.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("SIGUIENTE", style = typography.headlineMedium)
            }
        }

        // ---- Diálogo de validación ----
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Aceptar")
                    }
                },
                title = { Text("Aviso") },
                text = { Text(dialogMessage) }
            )
        }
    }
}

fun esMayorDeEdad(dia: Int, mes: Int, anio: Int): Boolean {
    val hoy = Calendar.getInstance()
    val nacimiento = Calendar.getInstance()
    nacimiento.set(anio, mes - 1, dia) // mes empieza en 0 en Calendar

    var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
    if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
        edad--
    }
    return edad >= 18
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewCuestionario1() = RoomieMatchUTheme { Cuestionario1Screen() }
 */