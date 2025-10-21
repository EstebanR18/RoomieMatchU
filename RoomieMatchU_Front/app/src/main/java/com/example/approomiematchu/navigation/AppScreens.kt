package com.example.approomiematchu.navigation

sealed class AppScreens(val route: String) {

    // ----------- AUTENTICACIÓN / HOME -----------
    object LandingScreen : AppScreens("landing_screen")

    object AuthScreen : AppScreens("auth_screen/{startInLogin}") {
        fun createRoute(startInLogin: Boolean) = "auth_screen/$startInLogin"
    }

    object EnterEmail : AppScreens("enter_email")
    object EnterCode : AppScreens("enter_code")
    object NewPassword : AppScreens("new_password")
    object ProfileScreen : AppScreens("profile_screen")

    // ----------- FLUJO DE CUESTIONARIO -----------
    object CuestionarioRol : AppScreens("cuestionario_rol")
    object Cuestionario1 : AppScreens("cuestionario_1")
    object Cuestionario2 : AppScreens("cuestionario_2")
    object Cuestionario3 : AppScreens("cuestionario_3")
    object CuestionarioBuscoCasa : AppScreens("cuestionario_busco_casa")
    object CuestionarioTengoCasa : AppScreens("cuestionario_tengo_casa")
    object CuestionarioFotoPerfil : AppScreens("cuestionario_foto_perfil")
    object CuestionarioFotoCasa : AppScreens("cuestionario_foto_casa")
    object SubirFotos : AppScreens("subir_fotos")
    object CuestionarioCompletado : AppScreens("cuestionario_completado")

    // ----------- PRINCIPAL -----------
    object HomeScreen : AppScreens("home_screen")
}
