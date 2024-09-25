package com.laru.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.laru.settings.SettingsScreen
import kotlinx.serialization.Serializable


@Serializable
data object SettingsRoute

fun NavController.navigateToSettings() = navigate(SettingsRoute) {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

fun NavGraphBuilder.settingsScreen() = composable<SettingsRoute> {
    SettingsScreen()
}
