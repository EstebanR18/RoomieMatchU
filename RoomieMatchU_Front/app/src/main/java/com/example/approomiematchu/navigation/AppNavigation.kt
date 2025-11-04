package com.example.approomiematchu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.approomiematchu.data.remote.RetrofitClient
import com.example.approomiematchu.ui.DescriptionBuscoCasaScreen
import com.example.approomiematchu.ui.DescriptionTengoCasaScreen
import com.example.approomiematchu.ui.HomeScreen
import com.example.approomiematchu.ui.authentication.*
import com.example.approomiematchu.ui.LandingScreen
import com.example.approomiematchu.ui.profile.PerfilBuscoLugarScreen
import com.example.approomiematchu.ui.profile.PerfilEditarScreenBuscoLugar
import com.example.approomiematchu.ui.profile.PerfilEditarScreenTengoLugar
import com.example.approomiematchu.ui.profile.PerfilTengoLugarScreen
import com.example.approomiematchu.ui.ProfileScreen
import com.example.approomiematchu.viewmodel.HomeViewModel
import com.example.approomiematchu.viewmodel.HomeViewModelFactory
import com.example.approomiematchu.ui.profileform.*
import com.example.approomiematchu.viewmodel.AuthViewModel
import com.example.approomiematchu.viewmodel.PasswordResetViewModel
import com.example.approomiematchu.viewmodel.PerfilCuestionarioViewModel
import com.example.approomiematchu.viewmodel.PerfilCuestionarioViewModelFactory

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val passwordViewModel: PasswordResetViewModel = viewModel()
    val perfilCuestionarioViewModel: PerfilCuestionarioViewModel = viewModel(
        factory = PerfilCuestionarioViewModelFactory(RetrofitClient.instance)
    )
    val authViewModel: AuthViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(RetrofitClient.instance)
    )

    // Observar el userId del AuthViewModel
    val userId by authViewModel.userId.collectAsState()

    // Cargar el perfil cuando el userId esté disponible
    LaunchedEffect(userId) {
        if (userId != null) {
            homeViewModel.loadUserProfile(userId!!)
        }
    }

    // Observar el estado del home
    val homeState by homeViewModel.uiState.collectAsState()

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
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                userId = userId
            )
        }

        // ---------- PERFILES ----------
        composable(AppScreens.PerfilBuscoLugar.route) {
            val userProfile by homeViewModel.userProfile.collectAsState()
            val userData by homeViewModel.userData.collectAsState()

            PerfilBuscoLugarScreen(
                userProfile = userProfile,
                userData = userData,
                navController = navController
            )
        }

        composable(AppScreens.PerfilEditarBuscoLugar.route) {
            val userProfile by homeViewModel.userProfile.collectAsState()
            val userData by homeViewModel.userData.collectAsState()

            PerfilEditarScreenBuscoLugar(
                userProfile = userProfile,
                userData = userData,
                viewModel = perfilCuestionarioViewModel,
                homeViewModel = homeViewModel,
                navController = navController
            )
        }


        composable(AppScreens.PerfilTengoLugar.route) {
            val userProfile by homeViewModel.userProfile.collectAsState()
            val userData by homeViewModel.userData.collectAsState()

            PerfilTengoLugarScreen(
                userProfile = userProfile,
                userData = userData,
                navController = navController
            )
        }

        composable(AppScreens.PerfilEditarTengoLugar.route) {
            val userProfile by homeViewModel.userProfile.collectAsState()
            val userData by homeViewModel.userData.collectAsState()

            PerfilEditarScreenTengoLugar(
                userProfile = userProfile,
                userData = userData,
                homeViewModel = homeViewModel,
                navController = navController
            )
        }


        // ---------- DESCRIPCIONES ----------
        composable(AppScreens.DescripcionBuscoCasa.route) {
            DescriptionBuscoCasaScreen(
                onBackClick = { NavigationUtils.goBack(navController) }
            )
        }

        composable(AppScreens.DescripcionTengoCasa.route) {
            DescriptionTengoCasaScreen(
                onBackClick = { NavigationUtils.goBack(navController) }
            )
        }
    }
}
