package com.example.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")

    object Machines : Screen("machines")
    object Booking : Screen("booking")
    object Payment : Screen("payment")
    object Qr : Screen("qr")
}