package com.example.approomiematchu.navigation


import androidx.navigation.NavController

fun navigateToAuth(navController: NavController, startInLogin: Boolean) {
    navController.navigate(AppScreens.AuthScreen.createRoute(startInLogin)) {
        launchSingleTop = true
    }
}
