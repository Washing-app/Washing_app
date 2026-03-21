package com.example.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface MachinesNavigation {
    fun NavGraphBuilder.machinesGraph(navController: NavHostController)
}

interface BookingNavigation {
    fun NavGraphBuilder.bookingGraph(navController: NavHostController)
}

interface PaymentNavigation {
    fun NavGraphBuilder.paymentGraph(navController: NavHostController)
}

interface QrNavigation {
    fun NavGraphBuilder.qrGraph(navController: NavHostController)
}