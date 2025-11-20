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
import com.example.approomiematchu.ui.profileform.presentation.TipoPerfil
import com.example.approomiematchu.ui.state.PerfilUIState
import com.example.approomiematchu.viewmodel.AuthViewModel
import com.example.approomiematchu.viewmodel.MatchViewModel
import com.example.approomiematchu.viewmodel.MatchViewModelFactory
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
    val matchViewModel: MatchViewModel = viewModel(
        factory = MatchViewModelFactory(RetrofitClient.instance)
    )

    // Observar sesión
    val userId by authViewModel.userId.collectAsState()
    val homeState by homeViewModel.uiState.collectAsState()

    // → 1. Cargar perfil del usuario al iniciar sesión
    LaunchedEffect(userId) {
        if (userId != null) {
            homeViewModel.loadUserProfile(userId!!)
        }
    }

    // → 2. Cargar sugerencias SOLO cuando se sepa el tipo de perfil
    LaunchedEffect(homeState.tipoPerfil) {
        if (userId != null &&
            homeState.tipoPerfil != TipoPerfil.NONE
        ) {
            matchViewModel.cargarPerfiles(userId!!)
        }
    }

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
            AuthScreen(
                initialIsLogin = startInLogin,
                navController = navController,
                authViewModel = authViewModel
            )
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
            CuestionarioRolScreen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        composable(AppScreens.Cuestionario1.route) {
            Cuestionario1Screen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel,
                authViewModel = authViewModel
            )
        }

        composable(AppScreens.Cuestionario2.route) {
            Cuestionario2Screen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        composable(AppScreens.Cuestionario3.route) {
            Cuestionario3Screen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        composable(AppScreens.CuestionarioBuscoCasa.route) {
            CuestionarioBuscoCasaScreen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        composable(AppScreens.CuestionarioTengoCasa.route) {
            CuestionarioTengoCasaScreen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        composable(AppScreens.CuestionarioFotoPerfil.route) {
            CuestionarioFotoPerfilScreen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        composable(AppScreens.CuestionarioFotoCasa.route) {
            CuestionarioFotoCasaScreen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        composable(AppScreens.SubirFotos.route) {
            SubirFotosScreen(navController = navController, viewModel = perfilCuestionarioViewModel)
        }

        composable(AppScreens.CuestionarioCompletado.route) {
            CuestionarioCompletadoScreen(
                navController = navController,
                viewModel = perfilCuestionarioViewModel
            )
        }

        // ---------- PRINCIPAL ----------
        composable(
            route = "home?reload={reload}",
            arguments = listOf(
                navArgument("reload") { defaultValue = "false" }
            )
        ) { backStackEntry ->
            val reload = backStackEntry.arguments?.getString("reload") ?: "false"
            HomeScreen(
                reloadFlag = reload == "true",
                navController = navController,
                homeViewModel = homeViewModel,
                matchViewModel = matchViewModel,
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
            val matchState by matchViewModel.uiState.collectAsState()

            DescriptionBuscoCasaScreen(
                perfil = (matchState as? PerfilUIState.Data)?.perfil,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppScreens.DescripcionTengoCasa.route) {
            val matchState by matchViewModel.uiState.collectAsState()

            DescriptionTengoCasaScreen(
                perfil = (matchState as? PerfilUIState.Data)?.perfil,
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}
