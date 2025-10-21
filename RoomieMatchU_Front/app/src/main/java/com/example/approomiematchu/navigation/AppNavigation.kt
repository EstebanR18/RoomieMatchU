package com.example.approomiematchu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.approomiematchu.ui.HomeScreen
import com.example.approomiematchu.ui.authentication.*
import com.example.approomiematchu.ui.LandingScreen
import com.example.approomiematchu.ui.ProfileScreen
import com.example.approomiematchu.ui.profileconfig.*
import com.example.approomiematchu.ui.profileconfig.presentation.PerfilCuestionarioViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val passwordViewModel: PasswordResetViewModel = viewModel()
    val perfilCuestionarioViewModel: PerfilCuestionarioViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppScreens.LandingScreen.route
    ) {

        // ---------- AUTENTICACIÓN ----------
        composable(AppScreens.LandingScreen.route) {
            LandingScreen(navController = navController)
        }

        composable(
            route = AppScreens.AuthScreen.route,
            arguments = listOf(navArgument("startInLogin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val startInLogin = backStackEntry.arguments?.getBoolean("startInLogin") ?: true
            AuthScreen(initialIsLogin = startInLogin, navController = navController, authViewModel = authViewModel)
        }

        composable(AppScreens.EnterEmail.route) {
            EnterEmailScreen(navController = navController, viewModel = passwordViewModel)
        }

        composable(AppScreens.EnterCode.route) {
            EnterCodeScreen(navController = navController, viewModel = passwordViewModel)
        }

        composable(AppScreens.NewPassword.route) {
            NewPasswordScreen(navController = navController, viewModel = passwordViewModel)
        }

        // ---------- CUESTIONARIO ----------
        composable(AppScreens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }

        composable(AppScreens.CuestionarioRol.route) {
            CuestionarioRolScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.Cuestionario1.route) {
            Cuestionario1Screen(navController = navController, viewModel = perfilCuestionarioViewModel, authViewModel = authViewModel)
        }

        composable(AppScreens.Cuestionario2.route) {
            Cuestionario2Screen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.Cuestionario3.route) {
            Cuestionario3Screen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.CuestionarioBuscoCasa.route) {
            CuestionarioBuscoCasaScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.CuestionarioTengoCasa.route) {
            CuestionarioTengoCasaScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.CuestionarioFotoPerfil.route) {
            CuestionarioFotoPerfilScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.CuestionarioFotoCasa.route) {
            CuestionarioFotoCasaScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.SubirFotos.route) {
            SubirFotosScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.CuestionarioCompletado.route) {
            CuestionarioCompletadoScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        // ---------- PRINCIPAL ----------
        composable(AppScreens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
    }
}
