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
fun Cuestionario1Screen() {
    CuestionarioBackground {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(current = 1)

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
            QuestionWithIcon(
                icon = Icons.Default.Person,
                text = "Nombre de usuario"
            )
            WhiteTextField(placeholder = "Escribe tu nombre")

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Cumpleaños ----
            QuestionWithIcon(icon = Icons.Default.CalendarMonth, text = "Cumpleaños")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("DD", "MM", "AA").forEach {
                    WhiteTextField(
                        placeholder = it,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Género ----
            QuestionWithIcon(icon = Icons.Default.Wc, text = "Género")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("Femenino", "Masculino", "Otro").forEach {
                    WhiteOutlinedButton(text = it)
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
                Text("SIGUIENTE")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCuestionario1() = RoomieMatchUTheme { Cuestionario1Screen() }