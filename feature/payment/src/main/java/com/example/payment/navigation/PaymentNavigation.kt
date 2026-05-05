package com.example.payment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.navigation.Screen
import com.example.payment.ui.PaymentScreen

fun NavGraphBuilder.paymentGraph(navController: NavController) {
    composable(
        route = Screen.Payment.route,
        arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
    ) { backStackEntry ->
        val bookingId = backStackEntry.arguments?.getString("bookingId") ?: return@composable

        PaymentScreen(
            bookingId = bookingId,
            onBackToMachines = {
                navController.navigate(Screen.Machines.route) {
                    popUpTo(Screen.Machines.route) { inclusive = false }
                    launchSingleTop = true
                }
            }
        )
    }
}