package com.example.approomiematchu.navigation


sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens("home_screen")

    object AuthScreen : AppScreens("auth_screen/{startInLogin}") {
        fun createRoute(startInLogin: Boolean) = "auth_screen/$startInLogin"

    }

    object EnterEmail : AppScreens("enter_email")
    object EnterCode : AppScreens("enter_code")
    object NewPassword : AppScreens("new_password")

}