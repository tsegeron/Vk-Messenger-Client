package com.laru.chats.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.laru.chats.ChatsScreen
import kotlinx.serialization.Serializable


@Serializable data object ChatsNavigation
@Serializable data object ChatsRoute

fun NavController.navigateToChats() = navigate(ChatsNavigation) {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

fun NavGraphBuilder.chatsScreenNavigation(onChatClick: (Int) -> Unit, routes: NavGraphBuilder.() -> Unit) =
    navigation<ChatsNavigation>(startDestination = ChatsRoute) {

        chatsScreen(onChatClick = onChatClick)

        routes()
    }

private fun NavGraphBuilder.chatsScreen(onChatClick: (Int) -> Unit) =
    composable<ChatsRoute> {
        ChatsScreen(onChatClick = onChatClick)
    }
