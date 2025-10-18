package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun CuestionarioTengoCasaScreen() {
    CuestionarioBackground {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProgressDots(current = 5)

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "SI ESCOGES BUSCO CASA",
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

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Zona o barrio preferido
                QuestionWithIcon(icon = Icons.Default.Person, text = "Zona o barrio de residencia")
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
                    ).forEach { WhiteOutlinedButton(text = it) }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Número de habitaciones
                QuestionWithIcon(icon = Icons.Default.Bed, text = "Número de habitaciones disponibles")
                WhiteTextField(
                    placeholder = "Ingresa el número de habitaciones"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Precio por habitación
                QuestionWithIcon(icon = Icons.Default.Home, text = "Precio del arriendo por habitación")
                WhiteTextField(
                    placeholder = "Ingresa el precio por habitación"
                )


                Spacer(modifier = Modifier.height(16.dp))


                // Servicios indispensables
                QuestionWithIcon(icon = Icons.Default.Home, text = "Servicios indispensables (Selecciona varias opciones)")
                val servicios = listOf(
                    "Internet", "Agua Caliente", "Lavadora",
                    "Secadora", "Amoblado básico", "Baño privado",
                    "Televisión", "Cocina equipada", "Nevera compartida",
                    "Parqueadero", "Espacios comunes (sala, comedor)", "Acceso inclusivo"
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    servicios.forEach { WhiteOutlinedButton(text = it) }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reglas básicas de la casa
                QuestionWithIcon(icon = Icons.Default.Warning, text = "Reglas básicas de la casa (Selecciona varias opciones)")
                val reglas = listOf("Visitas", "Horarios", "Fiestas", "Limpieza", "Se aceptan mascotas", "Cocina", "Ruido")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    reglas.forEach { WhiteOutlinedButton(text = it) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "SIGUIENTE",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCuestionarioTengoCasaScreen() = RoomieMatchUTheme { CuestionarioTengoCasaScreen() }
