package com.example.payment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.navigation.Screen
import com.example.payment.ui.PaymentScreen

fun NavGraphBuilder.paymentGraph(navController: NavController) {
    composable(Screen.Payment.route) {
        PaymentScreen(
            onQrClick = {
                navController.navigate(Screen.Qr.route)
            }
        )
    }
}