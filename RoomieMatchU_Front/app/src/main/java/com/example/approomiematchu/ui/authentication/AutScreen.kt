package com.example.approomiematchu.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.approomiematchu.R
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.ui.theme.AppTypography

@Composable
fun AuthScreen(initialIsLogin: Boolean = true, navController: NavController, authViewModel: AuthViewModel) {
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
                LoginForm(navController, authViewModel)
            } else {
                RegisterForm(navController, authViewModel)
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
fun LoginForm(navController: NavController, viewModel: AuthViewModel) {
    val email by viewModel.email
    val password by viewModel.password
    val isLoading by viewModel.isLoading

    val context = LocalContext.current
    val error = viewModel.errorMessage.value

    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.errorMessage.value = null
        }
    }


    Column(modifier = Modifier.fillMaxWidth()) {
        AuthTextField(
            title = "Correo electrónico",
            value = email,
            onValueChange = { viewModel.email.value = it },
            placeholder = "Ingrese correo",
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        Spacer(Modifier.height(10.dp))
        AuthTextField(
            title = "Contraseña",
            value = password,
            onValueChange = { viewModel.password.value = it },
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
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = { navController.navigate(AppScreens.EnterEmail.route)  }) {
                Text(
                    "¿Olvidaste la contraseña?",
                    style = AppTypography.subtitulo,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.login(
                    onNavigateToHome = {
                        navController.navigate(AppScreens.HomeScreen.route)
                    },
                    onNavigateToProfileSetup = {
                        navController.navigate(AppScreens.ProfileScreen.route)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            enabled = !isLoading
        ) {
            Text(
                if (isLoading) "Cargando..." else "INICIAR SESIÓN",
                style = AppTypography.titulo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun RegisterForm(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val fullName by viewModel.fullName
    val username by viewModel.username
    val phone by viewModel.phone
    val email by viewModel.email
    val password by viewModel.password
    val isLoading by viewModel.isLoading

    val context = LocalContext.current
    val error = viewModel.errorMessage.value

    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.errorMessage.value = null
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        AuthTextField(
            title = "Nombre completo",
            value = fullName,
            onValueChange = { viewModel.fullName.value = it },
            placeholder = "Ingrese nombre completo",
            leadingIcon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(10.dp))

        AuthTextField(
            title = "Correo electrónico",
            value = email,
            onValueChange = { viewModel.email.value = it },
            placeholder = "Ingrese correo",
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(10.dp))

        AuthTextField(
            title = "Teléfono",
            value = phone,
            onValueChange = { viewModel.phone.value = it },
            placeholder = "Ingrese número de teléfono",
            leadingIcon = {
                Icon(
                    Icons.Filled.Phone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(10.dp))

        AuthTextField(
            title = "Usuario",
            value = username,
            onValueChange = { viewModel.username.value = it },
            placeholder = "Ingrese nombre de usuario",
            leadingIcon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(Modifier.height(10.dp))

        AuthTextField(
            title = "Contraseña",
            value = password,
            onValueChange = { viewModel.password.value = it },
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


        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.register {
                    navController.navigate(AppScreens.ProfileScreen.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            enabled = !isLoading
        ) {
            Text(
                if (isLoading) "Cargando..." else "REGISTRARSE",
                style = AppTypography.titulo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


/*
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
 */
