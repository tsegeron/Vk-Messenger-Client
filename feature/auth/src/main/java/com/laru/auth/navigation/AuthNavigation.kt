package com.laru.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.laru.auth.AuthScreen
import kotlinx.serialization.Serializable


@Serializable data object AuthRoute

fun NavController.navigateToAuth() = navigate(AuthRoute)

fun NavGraphBuilder.authScreen(proceedAuthorization: () -> Unit) {
    composable<AuthRoute> {
        AuthScreen(proceedAuthorization)
    }
}
