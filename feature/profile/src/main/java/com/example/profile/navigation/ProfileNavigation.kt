package com.example.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.navigation.Screen
import com.example.profile.ui.ProfileScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(Screen.Profile.route) {
        ProfileScreen()
    }
}