package com.laru.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.laru.app.navigation.ApplicationNavHost
import com.laru.app.presentation.AppStartState
import com.laru.app.presentation.ApplicationViewModel
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.auth.navigation.AuthNavHost
import com.laru.auth.navigation.AuthRoute
import com.laru.chat.ChatViewModel
import com.laru.chats.model.ChatsListItem
import com.laru.chats.navigation.ChatsRoute
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val applicationViewModel: ApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val appAuthState by applicationViewModel.appAuthState.collectAsState()

            VKMessengerTheme {
                when (appAuthState) {
                    AppStartState.Authorized -> ApplicationNavHost()
                    AppStartState.Unauthorized -> AuthNavHost()
                }
            }
        }
    }
}
