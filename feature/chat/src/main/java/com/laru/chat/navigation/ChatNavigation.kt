package com.laru.chat.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.laru.chat.ChatScreen
import com.laru.chat.ChatViewModel
import kotlinx.serialization.Serializable


@Serializable data class ChatRoute(val chatId: Int)

fun NavController.navigateToChat(chatId: Int) = navigate(ChatRoute(chatId))

fun NavGraphBuilder.chatScreen(
    onBackClick: () -> Unit,
     onChatInfoClick: () -> Unit,
) {
    composable<ChatRoute> {
        val arguments = it.toRoute<ChatRoute>()
        val chatViewModel: ChatViewModel = hiltViewModel(
            creationCallback = { factory: ChatViewModel.Factory ->
                factory.create(arguments.chatId)
            }
        )

        ChatScreen(
            onBackClick = onBackClick,
            onChatInfoClick = onChatInfoClick,
            chatViewModel = chatViewModel
        )
    }
}
