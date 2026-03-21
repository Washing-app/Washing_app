package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    builders: List<NavGraphBuilderProvider>
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        builders.forEach { builder ->
            builder(this, navController)
        }
    }
}