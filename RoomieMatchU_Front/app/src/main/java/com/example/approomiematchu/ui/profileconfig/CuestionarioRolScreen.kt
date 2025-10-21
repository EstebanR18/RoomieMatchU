package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.approomiematchu.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.approomiematchu.navigation.NavigationUtils
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel
import com.example.approomiematchu.ui.profileconfig.presentation.TipoPerfil
import com.example.approomiematchu.ui.theme.RoomieMatchUTheme
@Composable
fun CuestionarioRolScreen(
    navController: NavController,
    viewModel: PerfilCuestionarioViewModel
) {
    val state by viewModel.state.collectAsState()
    var selectedOption by remember { mutableStateOf<TipoPerfil?>(state.tipoPerfil.takeIf { it != TipoPerfil.NONE }) }

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
            // Indicador de progreso
            ProgressDots(total = 8, current = 1)

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
                onClick = {
                    selectedOption = TipoPerfil.TENGO_LUGAR
                    viewModel.setTipoPerfil(TipoPerfil.TENGO_LUGAR)
                },
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == TipoPerfil.TENGO_LUGAR)
                        colors.primary
                    else
                        colors.surface,
                    contentColor = if (selectedOption == TipoPerfil.TENGO_LUGAR)
                        colors.onPrimary
                    else
                        colors.primary
                )
            ) {
                Text(
                    "Tengo casa (debes ser propietario)",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = typography.headlineMedium
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
                onClick = {
                    selectedOption = TipoPerfil.BUSCO_LUGAR
                    viewModel.setTipoPerfil(TipoPerfil.BUSCO_LUGAR)
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == TipoPerfil.BUSCO_LUGAR)
                        colors.primary
                    else
                        colors.surface,
                    contentColor = if (selectedOption == TipoPerfil.BUSCO_LUGAR)
                        colors.onPrimary
                    else
                        colors.primary
                )
            ) {
                Text(
                    "Busco casa",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ---- Botón de siguiente ----
            Button(
                onClick = {
                    // Avanza al siguiente paso
                    viewModel.avanzarPaso()
                    NavigationUtils.navigateToNextStep(
                        navController = navController,
                        tipoPerfil = selectedOption ?: TipoPerfil.NONE,
                        pasoActual = 1
                    )
                },
                enabled = selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "SIGUIENTE",
                    style = typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/*
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewCuestionarioRolScreen() = RoomieMatchUTheme { CuestionarioRolScreen() }
 */