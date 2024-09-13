package com.laru.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.auth.ui.AuthScreen
import com.laru.chats.ui.ChatsScreen
import com.laru.chats.ui.model.ChatsListItem


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mockData = listOf(
            ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
            ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
            ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
        )
        enableEdgeToEdge()
        setContent {
            VKMessengerTheme {
//                ChatsScreen(mockData)
                AuthScreen()
            }
        }
    }
}
