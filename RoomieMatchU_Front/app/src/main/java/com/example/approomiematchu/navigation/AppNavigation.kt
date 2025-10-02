package com.example.approomiematchu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.approomiematchu.ui.screens.AuthScreen
import com.example.approomiematchu.ui.screens.HomeScreen
import com.example.approomiematchu.ui.screens.authentication.EnterCodeScreen
import com.example.approomiematchu.ui.screens.authentication.EnterEmailScreen
import com.example.approomiematchu.ui.screens.authentication.NewPasswordScreen


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeScreen.route
    ) {
        // Pantalla de inicio
        composable(AppScreens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        // Pantalla de autenticación con argumento
        composable(
            route = AppScreens.AuthScreen.route,
            arguments = listOf(navArgument("startInLogin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val startInLogin = backStackEntry.arguments?.getBoolean("startInLogin") ?: true
            AuthScreen(initialIsLogin = startInLogin, navController = navController)
        }

        composable(AppScreens.EnterEmail.route) {
            EnterEmailScreen(navController)
        }

        composable(AppScreens.EnterCode.route) {
            EnterCodeScreen(navController)
        }

        composable(AppScreens.NewPassword.route) {
            NewPasswordScreen(navController)
        }

    }
}
