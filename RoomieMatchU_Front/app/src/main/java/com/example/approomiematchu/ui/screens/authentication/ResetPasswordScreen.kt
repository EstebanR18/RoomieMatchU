package com.example.approomiematchu.ui.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.approomiematchu.R
import com.example.approomiematchu.ui.screens.AuthTextField
import com.example.approomiematchu.ui.theme.AppTypography

// --- Pantalla de ingresar correo
@Composable
fun EnterEmailScreen() {
    var email by remember { mutableStateOf("") }

    CommonResetPasswordBackground {
        Text("Olvidé la contraseña", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onSurface)

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            title = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "Ingrese su correo",
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { /* Acción de enviar código */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("ENVIAR CÓDIGO", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

// --- Pantalla de ingresar código
@Composable
fun EnterCodeScreen() {
    var code by remember { mutableStateOf("") }

    CommonResetPasswordBackground {
        Text("Olvidé la contraseña", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onSurface)

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            title = "Código de autenticación",
            value = code,
            onValueChange = { code = it },
            placeholder = "Ingrese código",
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { /* Acción de validar código */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("CONFIRMAR", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

// --- Pantalla de nueva contraseña
@Composable
fun NewPasswordScreen() {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    CommonResetPasswordBackground {
        Text("Olvidé la contraseña",
            style = AppTypography.titulo2,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            title = "Contraseña Nueva",
            value = password,
            onValueChange = { password = it },
            placeholder = "Ingrese contraseña",
            isPassword = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(10.dp))

        AuthTextField(
            title = "Confirmar Contraseña Nueva",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Repita la contraseña",
            isPassword = true,
            leadingIcon = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { /* Acción de restablecer contraseña */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("RESTABLECER", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

// --- Fondo común para las pantallas
@Composable
fun CommonResetPasswordBackground(content: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_reset),
            contentDescription = "Fondo recuperar contraseña",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 35.dp)
                .padding(top = 250.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEnterEmailScreen() {
    EnterEmailScreen()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewEnterCodeScreen() {
    EnterCodeScreen()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNewPasswordScreen() {
    NewPasswordScreen()
}
