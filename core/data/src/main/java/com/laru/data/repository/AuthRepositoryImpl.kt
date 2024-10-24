package com.laru.data.repository

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.laru.common.di.ApplicationScope
import com.laru.data.model.AuthState
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.friends.FriendsService
import com.vk.sdk.api.friends.dto.FriendsGetFieldsResponseDto
import com.vk.sdk.api.messages.MessagesService
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
//    @ApplicationScope private val coroutineScope: CoroutineScope,
): AuthRepository {
    override val authState = MutableStateFlow(AuthState.Waiting)
//    override val accessToken: MutableStateFlow<VKAccessToken?> = MutableStateFlow(null)

    override suspend fun loginSucceeded() {
        Log.i(TAG, "logged in")
        authState.update { AuthState.SignedIn }
    }

    override suspend fun loginFailed() {
        Log.i(TAG, "logging in failed")
        authState.update { AuthState.Failed }
    }

    override suspend fun logout() {
        Log.i(TAG, "logging out")
        authState.update { AuthState.Waiting }
        VK.logout()
        VK.clearAccessToken(appContext)
    }

//    override suspend fun saveAccessToken(accessToken: VKAccessToken) {
//
//    }


    companion object {
        const val TAG = "AuthRepositoryImpl"
        const val AUTH_TAG = "vkAuthCallback"
        const val TOKEN_REFRESH_TAG = "vkRefreshTokenCallback"

    }
}

