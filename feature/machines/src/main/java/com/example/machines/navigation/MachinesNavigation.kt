package com.example.machines.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.machines.ui.MachinesScreen
import com.example.navigation.Screen

fun NavGraphBuilder.machinesGraph(navController: NavController) {
    composable(Screen.Machines.route) {
        MachinesScreen(
            onItemClick = {
                navController.navigate("details")
            }
        )
    }

    composable("details") {
        // заглушка
    }
}