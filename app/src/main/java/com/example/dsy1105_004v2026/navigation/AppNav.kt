package com.example.dsy1105_004v2026.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dsy1105_004v2026.ui.home.DrawerMenu
import com.example.dsy1105_004v2026.ui.home.ConvenioScreen
import com.example.dsy1105_004v2026.ui.theme.HomeScreen
import com.example.dsy1105_004v2026.ui.venta.ListaVentasScreen
import com.example.dsy1105_004v2026.ui.venta.VentaFormScreen
import com.example.dsy1105_004v2026.ui.venta.VentaViewModel

@Composable
fun AppNav(
    hasCameraPermission: Boolean,
    onRequestCameraPermission: () -> Unit
) {
    val navController = rememberNavController()

    // Un solo ViewModel compartido entre la lista y el formulario
    val ventaViewModel: VentaViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            HomeScreen(navController = navController)
        }

        composable(
            route = "DrawerMenu/{username}",
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            DrawerMenu(username = username, navController = navController)
        }

        composable(
            route = "convenio/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id").orEmpty()
            ConvenioScreen(convenioId = id, navController = navController)
        }

        composable("listaVentas") {
            ListaVentasScreen(
                navController = navController,
                viewModel = ventaViewModel
            )
        }

        // id = 0 -> venta nueva, id > 0 -> editar venta existente
        composable(
            route = "ventaForm/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            VentaFormScreen(
                ventaId = id,
                navController = navController,
                viewModel = ventaViewModel,
                hasCameraPermission = hasCameraPermission,
                onRequestCameraPermission = onRequestCameraPermission
            )
        }
    }
}
