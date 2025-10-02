package com.example.approomiematchu.navigation


sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens("home_screen")

    object AuthScreen : AppScreens("auth_screen/{startInLogin}") {
        fun createRoute(startInLogin: Boolean) = "auth_screen/$startInLogin"

    }

}