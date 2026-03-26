package com.example.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Machines : Screen("machines")
    object Bookings : Screen("bookings")
    object Profile : Screen("profile")
    object Payment : Screen("payment")
    object Qr : Screen("qr")
    object Details : Screen("details/{id}") {
        fun createRoute(id: String) = "details/$id"
    }
    object BookingDetail : Screen("booking_detail/{id}") {
        fun createRoute(id: String) = "booking_detail/$id"
    }
}