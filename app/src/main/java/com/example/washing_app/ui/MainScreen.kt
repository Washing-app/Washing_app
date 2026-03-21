package com.example.washing_app.ui

import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.navigation.AppNavGraph
import com.example.washing_app.ui.component.BottomBar
import com.example.machines.navigation.machinesGraph
import com.example.booking.navigation.bookingGraph
import com.example.navigation.NavGraphBuilderProvider
import com.example.payment.navigation.paymentGraph
import com.example.qr.navigation.qrGraph
import com.example.auth.navigation.authGraph
import com.example.navigation.Screen


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val graphs: List<NavGraphBuilderProvider> = listOf(
        { builder, controller -> builder.authGraph(controller) },
        { builder, controller -> builder.machinesGraph(controller) },
        { builder, controller -> builder.bookingGraph(controller) },
        { builder, controller -> builder.paymentGraph(controller) },
        { builder, controller -> builder.qrGraph(controller) }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Auth.route) {
                BottomBar(navController)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            AppNavGraph(
                navController = navController,
                builders = graphs
            )
        }
    }
}