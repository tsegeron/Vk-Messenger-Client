package com.laru.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.laru.app.navigation.ApplicationNavHost
import com.laru.app.presentation.AppStartState
import com.laru.app.presentation.ApplicationViewModel
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.auth.navigation.AuthNavHost
import com.laru.data.NetworkConnectionManager
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var networkConnectionManager: NetworkConnectionManager
    private lateinit var authLauncher: ActivityResultLauncher<Collection<VKScope>>

    private val applicationViewModel: ApplicationViewModel by viewModels()
    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            applicationViewModel.loginFailed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        networkConnectionManager.startListeningNetworkState()
        VK.addTokenExpiredHandler(tokenTracker)

        Log.d("MainActivity", VK.isLoggedIn().toString())

        // remake as another AuthActivity class or as fragment?
        authLauncher = VK.login(this) { result : VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Log.i("MainActivity", result.token.accessToken)
                    applicationViewModel.loginSucceeded()
                }
                is VKAuthenticationResult.Failed -> {
                    applicationViewModel.loginFailed()
                }
            }
        }

        setContent {
            val appAuthState by applicationViewModel.appAuthState.collectAsState()

            VKMessengerTheme {
                when (appAuthState) {
                    AppStartState.Authorized -> ApplicationNavHost()
                    AppStartState.Unauthorized -> AuthNavHost(
                        { authLauncher.launch(arrayListOf(VKScope.FRIENDS, VKScope.MESSAGES)) }
                    )
                }
            }
        }
    }


    override fun onDestroy() {
        networkConnectionManager.stopListeningNetworkState()
        super.onDestroy()
    }
}

/*
    TODO: UI for friends screen
        1: manage photo tap -> navigation to profile
        2: manage photo press -> profile sneek peak
        3: manage row tap -> navigation to dialog
        4: manage row press -> dialog sneek peak

    TODO: ProfilePhoto as initials or something else when no network/photo
 */
