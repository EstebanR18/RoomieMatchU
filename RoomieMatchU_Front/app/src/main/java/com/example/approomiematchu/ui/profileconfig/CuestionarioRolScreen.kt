package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.approomiematchu.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme

@Composable
fun CuestionarioRolScreen() {
    CuestionarioBackground {
        val colors = MaterialTheme.colorScheme
        val typography = MaterialTheme.typography

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressDots(current = 4) // Paso 4

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                "TU ROL EN ROOMIE MATCH U",
                style = typography.displayLarge,
                color = colors.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                "Escoge si buscas casa o si tienes una casa para compartir.",
                style = typography.bodyLarge,
                color = colors.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            // ---- Tengo casa ----
                    Image(
                        painter = painterResource(id = R.drawable.ic_tengo_casa),
                        contentDescription = "Tengo casa",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Fit
                    )
            Button(
                onClick = {},
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Tengo casa (debes ser propietario)",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography. headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---- Busco casa ----
            Image(
                painter = painterResource(id = R.drawable.ic_busco_casa),
                contentDescription = "Busco casa",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Fit
            )
            Button(
                onClick = {},
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Busco casa",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography. headlineMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ---- Botón de siguiente ----
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "SIGUIENTE",
                    style = MaterialTheme.typography. headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewCuestionarioRolScreen() = RoomieMatchUTheme { CuestionarioRolScreen() }
