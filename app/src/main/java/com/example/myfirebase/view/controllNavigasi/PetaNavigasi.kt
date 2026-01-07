package com.example.myfirebase.view.controllNavigasi

import com.example.myfirebase.view.route.DestinasiEntry
import com.example.myfirebase.view.route.DestinasiHome
import com.example.myfirebase.view.route.DestinasiEdit
import com.example.myfirebase.view.route.DestinasiDetail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirebase.view.EntrySiswaScreen
import com.example.myfirebase.view.HomeScreen

@Suppress("unused")
@Composable
fun DataSiswaApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    HostNavigasi(navController = navController, modifier = modifier)
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = modifier
    ) {
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiEntry.route) },
                navigateToItemEdit = { id -> navController.navigate("${DestinasiEdit.route}/$id") },
                navigateToItemDetail = { id -> navController.navigate("${DestinasiDetail.route}/$id") }
            )
        }
        composable(DestinasiEntry.route) {
            EntrySiswaScreen(
                navigateBack = { navController.navigate(DestinasiHome.route) }
            )
        }
        composable(DestinasiEdit.routeWithArgs) { backStackEntry ->
            val idArg = backStackEntry.arguments?.getString(DestinasiEdit.itemIdArg)
            val id = idArg?.toLongOrNull() ?: 0L
            com.example.myfirebase.view.EditSiswaScreen(
                idSiswa = id,
                navigateBack = { navController.navigate(DestinasiHome.route) }
            )
        }
        composable(DestinasiDetail.routeWithArgs) { backStackEntry ->
            val idArg = backStackEntry.arguments?.getString(DestinasiDetail.itemIdArg)
            val id = idArg?.toLongOrNull() ?: 0L
            com.example.myfirebase.view.DetailSiswaScreen(
                idSiswa = id,
                navigateBack = { navController.navigate(DestinasiHome.route) }
            )
        }
    }
}