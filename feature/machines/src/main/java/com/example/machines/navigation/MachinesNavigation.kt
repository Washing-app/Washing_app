package com.example.machines.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.machines.ui.MachineDetailScreen
import com.example.machines.ui.MachinesScreen
import com.example.navigation.Screen

fun NavGraphBuilder.machinesGraph(
    navController: NavController
) {

    composable(Screen.Machines.route) {

        MachinesScreen(
            onItemClick = { program ->
                navController.navigate(Screen.Details.createRoute(program.id.toString()))
            }
        )
    }

    composable(
        route = Screen.Details.route,
        arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) { backStackEntry ->

        val id = backStackEntry.arguments?.getString("id")?: return@composable

        MachineDetailScreen(
            programId = id,
            navController = navController
        )
    }
}