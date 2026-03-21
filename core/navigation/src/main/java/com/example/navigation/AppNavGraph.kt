package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavGraph(
    navController: NavHostController,
    builders: List<NavGraphBuilderProvider>
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        builders.forEach { builder ->
            builder(this, navController)
        }
    }
}