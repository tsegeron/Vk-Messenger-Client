package com.laru.data.repository

import android.content.Context
import android.util.Log
import com.laru.data.model.AuthState
import com.vk.api.sdk.VK
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

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

