package com.example.approomiematchu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.approomiematchu.R
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun CuestionarioBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.15f))
        )
        content()
    }
}

@Composable
fun ProgressDots(total: Int = 6, current: Int) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .size(12.dp)
                    .background(
                        if (index < current) colors.secondary else colors.surface,
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

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

@Composable
fun Cuestionario2Screen() {
    CuestionarioBackground {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography
        var alergia by remember { mutableStateOf(false) }
        var mascotas by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(current = 2)

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
            YesNoButtons()

            // ---- ¿Eres alérgico? ----
            QuestionWithIcon(icon = Icons.Default.Healing, text = "¿Eres alérgico a algo?")
            YesNoButtons(onYes = { alergia = true }, onNo = { alergia = false })
            if (alergia) {
                WhiteTextField(placeholder = "¿A qué?")
            }

            // ---- ¿Tienes mascotas? ----
            QuestionWithIcon(icon = Icons.Default.Pets, text = "¿Tienes mascotas?")
            YesNoButtons(onYes = { mascotas = true }, onNo = { mascotas = false })
            if (mascotas) {
                WhiteTextField(placeholder = "¿Cuáles?")
            }

            // ---- ¿Dispuesto a vivir con mascotas? ----
            QuestionWithIcon(icon = Icons.Default.Home, text = "¿Estás dispuesto a vivir con mascotas?")
            YesNoButtons()

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
                Text("SIGUIENTE")
            }
        }
    }
}

@Composable
fun WhiteTextField(placeholder: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(placeholder) },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun WhiteOutlinedButton(text: String, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = {},
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text, textAlign = TextAlign.Center)
    }
}

@Composable
fun QuestionWithIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = typography.headlineMedium, color = colors.onBackground)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun YesNoButtons(onYes: () -> Unit = {}, onNo: () -> Unit = {}) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        WhiteOutlinedButton(text = "Sí", modifier = Modifier.weight(1f))
        WhiteOutlinedButton(text = "No", modifier = Modifier.weight(1f))
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewCuestionario1() = RoomieMatchUTheme { Cuestionario1Screen() }

@Preview(showBackground = true)
@Composable
fun PreviewCuestionario2() = RoomieMatchUTheme { Cuestionario2Screen() }

@Preview(showBackground = true)
@Composable
fun PreviewCuestionario3() = RoomieMatchUTheme { Cuestionario3Screen() }
