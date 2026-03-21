package com.example.qr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.navigation.Screen
import com.example.qr.ui.QrScreen

fun NavGraphBuilder.qrGraph(navController: NavController) {
    composable(Screen.Qr.route) {
        QrScreen()
    }
}