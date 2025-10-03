package com.example.approomiematchu.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.approomiematchu.R
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.ui.theme.AppTypography

// --- Pantalla de ingresar correo
@Composable
fun EnterEmailScreen(navController: NavController, viewModel: PasswordResetViewModel) {

    val context = LocalContext.current

    CommonResetPasswordBackground {
        Text("Olvidé la contraseña", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onSurface)

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            title = "Correo electrónico",
            value = viewModel.email.value,
            onValueChange = { viewModel.email.value = it },
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
            onClick = {
                viewModel.requestCode {
                    navController.navigate(AppScreens.EnterCode.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            enabled = !viewModel.isLoading.value,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (viewModel.isLoading.value) "ENVIANDO..." else "ENVIAR CÓDIGO", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onPrimary)
        }

        viewModel.errorMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.errorMessage.value = null
        }
        viewModel.successMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.successMessage.value = null
        }
    }
}

// --- Pantalla de ingresar código
@Composable
fun EnterCodeScreen(navController: NavController, viewModel: PasswordResetViewModel) {
    val context = LocalContext.current

    CommonResetPasswordBackground {
        Text("Olvidé la contraseña", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onSurface)

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            title = "Código de autenticación",
            value = viewModel.token.value,
            onValueChange = { viewModel.token.value = it },
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
            onClick = {
                viewModel.validateToken {
                    navController.navigate(AppScreens.NewPassword.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            enabled = !viewModel.isLoading.value,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (viewModel.isLoading.value) "VALIDANDO..." else "CONFIRMAR", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onPrimary)
        }

        viewModel.errorMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.errorMessage.value = null
        }
        viewModel.successMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.successMessage.value = null
        }
    }
}

// --- Pantalla de nueva contraseña
@Composable
fun NewPasswordScreen(navController: NavController, viewModel: PasswordResetViewModel) {
    val context = LocalContext.current

    CommonResetPasswordBackground {
        Text("Olvidé la contraseña",
            style = AppTypography.titulo2,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            title = "Contraseña Nueva",
            value = viewModel.newPassword.value,
            onValueChange = { viewModel.newPassword.value = it },
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
            value = viewModel.confirmPassword.value,
            onValueChange = { viewModel.confirmPassword.value = it },
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
            onClick = {
                viewModel.resetPassword {
                    navController.navigate(
                        AppScreens.AuthScreen.createRoute(startInLogin = true)
                    ) {
                        popUpTo(AppScreens.AuthScreen.route) { inclusive = true }
                    }
                }
            }
            ,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            enabled = !viewModel.isLoading.value,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (viewModel.isLoading.value) "GUARDANDO..." else "RESTABLECER", style = AppTypography.titulo2, color = MaterialTheme.colorScheme.onPrimary)
        }

        viewModel.errorMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.errorMessage.value = null
        }
        viewModel.successMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.successMessage.value = null
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

/*
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

 */
