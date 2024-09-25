package com.laru.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.common.preview.ThemePreviews
import com.laru.ui.model.Paddings
import kotlinx.serialization.Serializable


@Composable
fun ChatScreen(
    onBackClick: () -> Unit,
    onChatInfoClick: () -> Unit,
    chatViewModel: ChatViewModel = hiltViewModel(),
) {
    BackHandler {
        onBackClick()
    }

    Scaffold() { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = onChatInfoClick,
                modifier = Modifier
                    .padding(horizontal = Paddings.large)
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Chat ${chatViewModel.chatId} Screen")
            }
        }
    }
}


@ThemePreviews
@Composable
fun ChatScreenPreview(modifier: Modifier = Modifier) {
    VKMessengerTheme {
        ChatScreen({}, {})
    }
}
