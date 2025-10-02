package com.example.approomiematchu.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.approomiematchu.R
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.ui.theme.AppTypography
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen superior
            Image(
                painter = painterResource(id = R.drawable.home_illustration),
                contentDescription = "Roomie Illustration",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 50.dp)
            )

            // Texto de bienvenida
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = "BIENVENIDO A",
                    style = AppTypography.titulo2,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "ROOMIE MATCH U",
                    style = AppTypography.titulo1,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Botones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Iniciar Sesión
                Button(
                    onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(true))},
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        style = AppTypography.titulo2,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Botón Registrarse
                OutlinedButton(
                    onClick = { navController.navigate(AppScreens.AuthScreen.createRoute(false))},
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(48.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "REGISTRARSE",
                        style = AppTypography.titulo2,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RoomieMatchUTheme {
        HomeScreen(navController = rememberNavController())
    }
}
