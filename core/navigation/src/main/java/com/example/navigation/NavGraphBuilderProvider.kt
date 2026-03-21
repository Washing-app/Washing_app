package com.example.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

typealias NavGraphBuilderProvider =
            (NavGraphBuilder, NavController) -> Unit