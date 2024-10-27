package com.laru.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.laru.chat.navigation.chatScreen
import com.laru.chat.navigation.navigateToChat
import com.laru.chatinfo.navigation.chatInfoScreen
import com.laru.chatinfo.navigation.navigateToChatInfo
import com.laru.chats.navigation.ChatsNavigation
import com.laru.chats.navigation.ChatsRoute
import com.laru.chats.navigation.chatsScreenNavigation
import com.laru.chats.navigation.navigateToChats
import com.laru.friends.navigation.FriendsRoute
import com.laru.friends.navigation.friendsScreen
import com.laru.friends.navigation.navigateToFriends
import com.laru.settings.navigation.SettingsRoute
import com.laru.settings.navigation.navigateToSettings
import com.laru.settings.navigation.settingsScreen
import kotlin.reflect.KClass


/**
 * top level NavHost
 */
@Composable
fun ApplicationNavHost(
    navHostController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val isBottomBarShown = currentDestination != null && (currentDestination.hasRoute<FriendsRoute>() ||
            currentDestination.hasRoute<ChatsRoute>() || currentDestination.hasRoute<SettingsRoute>())

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        NavHost(
            navController = navHostController,
            startDestination = ChatsNavigation,
            modifier = Modifier.weight(1f)
        ) {
            chatsScreenNavigation(onChatClick = navHostController::navigateToChat) {
                chatScreen(
                    onBackClick = navHostController::popBackStack,
                    onChatInfoClick = navHostController::navigateToChatInfo
                )
                chatInfoScreen(onBackClick = navHostController::popBackStack)
            }

            friendsScreen(onFriendClick = navHostController::navigateToChat)

            settingsScreen()
        }

        if (isBottomBarShown) {
            NavigationBar(
                modifier = Modifier.consumeWindowInsets(WindowInsets.navigationBars), // remove??
            ) {
                TopLevelDestination.entries.forEach { destination ->
                    val isSelected = currentDestination.isRouteInHierarchy(destination.route)

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = when (destination) {
                            TopLevelDestination.Friends -> navHostController::navigateToFriends
                            TopLevelDestination.Chats -> navHostController::navigateToChats
                            TopLevelDestination.Settings -> navHostController::navigateToSettings
                        },
                        icon = { Icon(
                            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = stringResource(destination.iconTextId)
                        ) },
                        label = { Text(text = stringResource(destination.titleTextId)) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any { it.hasRoute(route) } ?: false
