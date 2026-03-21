package com.example.booking.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.booking.ui.BookingScreen
import com.example.navigation.Screen

fun NavGraphBuilder.bookingGraph(navController: NavController) {
    composable(Screen.Booking.route) {
        BookingScreen(
            onPaymentClick = {
                navController.navigate(Screen.Payment.route)
            }
        )
    }
}