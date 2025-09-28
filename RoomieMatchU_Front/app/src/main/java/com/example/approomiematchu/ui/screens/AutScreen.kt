package com.example.approomiematchu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun AuthScreen(initialIsLogin: Boolean = true) {
    var isLoginSelected by remember { mutableStateOf(initialIsLogin) }


    Box( // 👈 Box raíz con fondo
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 100.dp),
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
                fontSize = 18.sp,
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
                fontSize = 18.sp,
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
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = MaterialTheme.colorScheme.secondary // 👈 color del ojo
                        )
                    }
                } else {
                    trailingIcon?.invoke()
                }
            },
            visualTransformation = if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthTextField(
            title = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "Ingrese correo",
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            title = "Contraseña",
            value = password,
            onValueChange = { password = it },
            placeholder = "Ingrese contraseña",
            isPassword = true,
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = { /* sin lógica */ }) {
                Text("¿Olvidaste la contraseña?", color = MaterialTheme.colorScheme.secondary)
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { /* solo UI */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("INICIAR SESIÓN", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun RegisterForm() {
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthTextField(
            title = "Nombre completo",
            value = username,
            onValueChange = { username = it },
            placeholder = "Ingrese nombre completo",
            leadingIcon = {
                Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        )
        Spacer(Modifier.height(16.dp))

        AuthTextField(
            title = "Teléfono",
            value = phone,
            onValueChange = { phone = it },
            placeholder = "Ingrese número de teléfono",
            leadingIcon = {
                Icon(Icons.Filled.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            title = "Correo electrónico",
            value = email,
            onValueChange = { email = it },
            placeholder = "Ingrese correo",
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        )
        Spacer(Modifier.height(16.dp))
        AuthTextField(
            title = "Contraseña",
            value = password,
            onValueChange = { password = it },
            placeholder = "Ingrese contraseña",
            isPassword = true,
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { /* solo UI */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("REGISTRARSE", color = MaterialTheme.colorScheme.onPrimary)
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
