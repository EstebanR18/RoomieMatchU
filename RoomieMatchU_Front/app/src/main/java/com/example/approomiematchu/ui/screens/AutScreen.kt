package com.example.approomiematchu.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.approomiematchu.R
import com.example.approomiematchu.ui.theme.AppTypography
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun AuthScreen(initialIsLogin: Boolean = true) {
    var isLoginSelected by remember { mutableStateOf(initialIsLogin) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = "Fondo Login",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )

        // Formulario sobre el fondo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 35.dp)
                .padding(top = 250.dp), // Ajusta el inicio del formulario
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(
                isLoginSelected = isLoginSelected,
                onLoginClick = { isLoginSelected = true },
                onRegisterClick = { isLoginSelected = false }
            )
            Spacer(Modifier.height(32.dp))
            if (isLoginSelected) {
                LoginForm()
            } else {
                RegisterForm()
            }
        }
    }
}

@Composable
fun AuthHeader(
    isLoginSelected: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onLoginClick() }
        ) {
            Text(
                text = "Iniciar Sesión",
                style = AppTypography.titulo2,
                color = if (isLoginSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary
            )
            if (isLoginSelected) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(120.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onRegisterClick() }
        ) {
            Text(
                text = "Registrarse",
                style = AppTypography.titulo2,
                color = if (!isLoginSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary
            )
            if (!isLoginSelected) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(120.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

@Composable
fun AuthTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = AppTypography.subtitulo,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = AppTypography.normalText,
            placeholder = { Text(placeholder, style = AppTypography.normalText) },
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                } else {
                    trailingIcon?.invoke()
                }
            },
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthTextField(
            title = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "Ingrese correo",
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )
        Spacer(Modifier.height(10.dp))
        AuthTextField(
            title = "Contraseña",
            value = password,
            onValueChange = { password = it },
            placeholder = "Ingrese contraseña",
            isPassword = true,
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = { /* sin lógica */ }) {
                Text(
                    "¿Olvidaste la contraseña?",
                    style = AppTypography.subtitulo,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        errorMessage?.let {
            Spacer(Modifier.height(10.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.secondary,
                style = AppTypography.subtitulo,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { /* solo UI */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                "INICIAR SESIÓN",
                style = AppTypography.titulo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun RegisterForm() {
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthTextField(
            title = "Nombre completo",
            value = username,
            onValueChange = { username = it },
            placeholder = "Ingrese nombre completo",
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )
        Spacer(Modifier.height(10.dp))
        AuthTextField(
            title = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "Ingrese correo",
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )
        Spacer(Modifier.height(10.dp))
        AuthTextField(
            title = "Contraseña",
            value = password,
            onValueChange = { password = it },
            placeholder = "Ingrese contraseña",
            isPassword = true,
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )
        errorMessage?.let {
            Spacer(Modifier.height(25.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.secondary,
                style = AppTypography.subtitulo,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { /* solo UI */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                "REGISTRARSE",
                style = AppTypography.titulo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LoginPreview() {
    RoomieMatchUTheme {
        AuthScreen(initialIsLogin = true)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun RegisterPreview() {
    RoomieMatchUTheme {
        AuthScreen(initialIsLogin = false)
    }
}
