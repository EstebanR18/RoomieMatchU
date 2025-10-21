package com.example.approomiematchu.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.approomiematchu.R
import com.example.approomiematchu.navigation.AppScreens
import com.example.approomiematchu.ui.theme.AppTypography
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun ProfileScreen(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_profile),
            contentDescription = "Fondo Profile",
            modifier = Modifier.matchParentSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
            .fillMaxSize()
            .padding( 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen superior
            Image(
                painter = painterResource(id = R.drawable.profile_icon),
                contentDescription = "Roomie Illustration",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 120.dp)
            )

            // Texto de bienvenida
            Column(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = "CONSTRUYE",
                    style = AppTypography.titulo2,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "TU PERFIL",
                    style = AppTypography.titulo1,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

                // Botón Iniciar Sesión
                Button(
                    onClick = { navController.navigate(AppScreens.CuestionarioRol.route)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "SIGUIENTE",
                        style = AppTypography.titulo2,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    RoomieMatchUTheme {
        val navController = rememberNavController()
        ProfileScreen(navController = navController)
    }
}