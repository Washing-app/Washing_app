package com.example.washing_app.ui.component

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun BottomBar(navController: NavHostController) {

    val items = listOf(
        Screen.Bookings,
        Screen.Machines,
        Screen.Qr,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {

        items.forEach { screen ->

            NavigationBarItem(
                selected = currentRoute == screen.route,

                onClick = {
                    navController.navigate(screen.route) {

                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.Bookings -> Icons.Default.CalendarMonth
                            Screen.Machines -> Icons.Default.Home
                            Screen.Qr -> Icons.Default.QrCode
                            Screen.Profile -> Icons.Default.AccountCircle
                            else -> Icons.Default.Home
                        },
                        contentDescription = screen.route
                    )
                },

                label = {
                    Text(
                        when (screen) {
                            Screen.Bookings -> "Мои записи"
                            Screen.Machines -> "Главная"
                            Screen.Qr -> "QR"
                            Screen.Profile -> "Профиль"
                            else -> ""
                        }
                    )
                }
            )
        }
    }
}