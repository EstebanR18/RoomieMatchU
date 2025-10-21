package com.example.approomiematchu.navigation

import androidx.navigation.NavController
import com.example.approomiematchu.ui.profileconfig.presentation.TipoPerfil

object NavigationUtils {

    /**
     * Navega a una pantalla específica usando su ruta.
     */
    fun navigateToScreen(navController: NavController, route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    /**
     * Navega y limpia el backstack (para pantallas finales o de flujo cerrado)
     */
    fun navigateAndClear(navController: NavController, route: String) {
        navController.navigate(route) {
            popUpTo(0) // limpia todo el historial
            launchSingleTop = true
        }
    }

    /**
     * Vuelve atrás en la pila de navegación.
     */
    fun goBack(navController: NavController) {
        navController.popBackStack()
    }

    /**
     * Redirige al flujo de autenticación con parámetro (login/register).
     */
    fun navigateToAuth(navController: NavController, startInLogin: Boolean) {
        navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin)) {
            launchSingleTop = true
        }
    }

    /**
     * Avanza al siguiente paso del cuestionario según el tipo de perfil.
     */
    fun navigateToNextStep(navController: NavController, tipoPerfil: TipoPerfil, pasoActual: Int) {
        val nextRoute = when (tipoPerfil) {

            // ---------------- BUSCO LUGAR ----------------
            TipoPerfil.BUSCO_LUGAR -> when (pasoActual) {
                1 -> AppScreens.Cuestionario1.route
                2 -> AppScreens.Cuestionario2.route
                3 -> AppScreens.Cuestionario3.route
                4 -> AppScreens.CuestionarioBuscoCasa.route
                5 -> AppScreens.CuestionarioFotoPerfil.route
                6 -> AppScreens.CuestionarioCompletado.route
                else -> AppScreens.ProfileScreen.route
            }

            // ---------------- TENGO LUGAR ----------------
            TipoPerfil.TENGO_LUGAR -> when (pasoActual) {
                1 -> AppScreens.Cuestionario1.route
                2 -> AppScreens.Cuestionario2.route
                3 -> AppScreens.Cuestionario3.route
                4 -> AppScreens.CuestionarioTengoCasa.route
                5 -> AppScreens.CuestionarioFotoPerfil.route
                6 -> AppScreens.CuestionarioFotoCasa.route
                7 -> AppScreens.SubirFotos.route
                8 -> AppScreens.CuestionarioCompletado.route
                else -> AppScreens.ProfileScreen.route
            }

            else -> AppScreens.CuestionarioRol.route
        }

        navController.navigate(nextRoute)
    }


    /**
     * Retrocede un paso en el flujo del cuestionario.
     */
    fun navigateToPreviousStep(navController: NavController, tipoPerfil: TipoPerfil, pasoActual: Int) {
        val previousRoute = when (tipoPerfil) {

            // ---------------- BUSCO LUGAR ----------------
            TipoPerfil.BUSCO_LUGAR -> when (pasoActual) {
                2 -> AppScreens.CuestionarioRol.route
                3 -> AppScreens.Cuestionario1.route
                4 -> AppScreens.Cuestionario2.route
                5 -> AppScreens.Cuestionario3.route
                6 -> AppScreens.CuestionarioBuscoCasa.route
                7 -> AppScreens.CuestionarioFotoPerfil.route
                else -> AppScreens.LandingScreen.route
            }

            // ---------------- TENGO LUGAR ----------------
            TipoPerfil.TENGO_LUGAR -> when (pasoActual) {
                2 -> AppScreens.CuestionarioRol.route
                3 -> AppScreens.Cuestionario1.route
                4 -> AppScreens.Cuestionario2.route
                5 -> AppScreens.Cuestionario3.route
                6 -> AppScreens.CuestionarioTengoCasa.route
                7 -> AppScreens.CuestionarioFotoPerfil.route
                8 -> AppScreens.CuestionarioFotoCasa.route
                else -> AppScreens.LandingScreen.route
            }

            else -> AppScreens.LandingScreen.route
        }

        navController.navigate(previousRoute)
    }
}
