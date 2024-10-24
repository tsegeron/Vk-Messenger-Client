package com.laru.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Composable
fun AuthNavHost(
    proceedAuthorization: () -> Unit,
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navHostController,
        startDestination = AuthRoute
    ) {
        authScreen(proceedAuthorization)
    }
}
