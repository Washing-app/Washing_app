package com.example.booking.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.booking.ui.BookingDetailScreen
import com.example.booking.ui.BookingDetailViewModel
import com.example.booking.ui.BookingScreen
import com.example.navigation.Screen

fun NavGraphBuilder.bookingsGraph(navController: NavController) {
    composable(Screen.Bookings.route) {
        BookingScreen(
            onBookingClick = { bookingId ->
                navController.navigate(Screen.BookingDetail.createRoute(bookingId))
            }
        )
    }

    composable(
        route = Screen.BookingDetail.route,
        arguments = listOf(
            navArgument("id") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val bookingId = backStackEntry.arguments?.getString("id") ?: return@composable
        val viewModel: BookingDetailViewModel = hiltViewModel()

        BookingDetailScreen(
            bookingId = bookingId,
            navController = navController,
            viewModel = viewModel
        )
    }
}