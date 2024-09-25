package com.laru.chatinfo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.laru.chatinfo.ChatInfoScreen
import kotlinx.serialization.Serializable


@Serializable data object ChatInfoRoute

fun NavController.navigateToChatInfo() = navigate(ChatInfoRoute) // ...

fun NavGraphBuilder.chatInfoScreen(onBackClick: () -> Unit) = composable<ChatInfoRoute> {
    ChatInfoScreen(onBackClick = onBackClick)
}
