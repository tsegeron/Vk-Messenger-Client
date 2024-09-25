package com.laru.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.laru.chats.navigation.ChatsRoute
import com.laru.friends.navigation.FriendsRoute
import com.laru.settings.navigation.SettingsRoute
import com.laru.friends.R as friendsR
import com.laru.chats.R as chatsR
import com.laru.settings.R as settingsR
import kotlin.reflect.KClass


enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
//    val navigateTo: NavController.() -> Unit
) {
    Friends(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = friendsR.string.feature_friends_title,
        titleTextId = friendsR.string.feature_friends_title,
        route = FriendsRoute::class,
    ),
    Chats(
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email,
        iconTextId = chatsR.string.feature_chats_title,
        titleTextId = chatsR.string.feature_chats_title,
        route = ChatsRoute::class,
    ),
    Settings(
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        iconTextId = settingsR.string.feature_settings_title,
        titleTextId = settingsR.string.feature_settings_title,
        route = SettingsRoute::class,
    ),
}
