package com.laru.friends.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.laru.friends.FriendsScreen
import kotlinx.serialization.Serializable


@Serializable data object FriendsRoute

fun NavController.navigateToFriends() = navigate(FriendsRoute) {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

fun NavGraphBuilder.friendsScreen(onFriendClick: (Long) -> Unit) = composable<FriendsRoute> {
    FriendsScreen(onFriendClick = onFriendClick)
}
