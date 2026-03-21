package com.example.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.auth.ui.AuthScreen
import com.example.navigation.Screen

fun NavGraphBuilder.authGraph(navController: NavController) {
    composable(Screen.Auth.route) {
        AuthScreen(
            onSuccess = {
                navController.navigate(Screen.Machines.route) {
                    popUpTo(Screen.Auth.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}