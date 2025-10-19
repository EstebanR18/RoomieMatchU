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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun Cuestionario3Screen() {
    CuestionarioBackground {
        val typography = MaterialTheme.typography

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(current = 3)

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "PRESUPUESTO Y TIEMPO",
                style = typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                "Indícanos cuánto puedes invertir y por cuánto tiempo necesitas quedarte.",
                style = typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            QuestionWithIcon(icon = Icons.Default.AttachMoney, text = "Presupuesto mensual aproximado")
            WhiteTextField(placeholder = "Ejemplo: $800.000")

            Spacer(modifier = Modifier.height(16.dp))

            QuestionWithIcon(icon = Icons.Default.Schedule, text = "Tiempo estimado de estancia")
            Column(modifier = Modifier.fillMaxWidth()) {
                listOf("Menos de 6 meses", "6-12 meses", "Más de 1 año").forEach {
                    WhiteOutlinedButton(text = it, modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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
fun PreviewCuestionario3() = RoomieMatchUTheme { Cuestionario3Screen() }