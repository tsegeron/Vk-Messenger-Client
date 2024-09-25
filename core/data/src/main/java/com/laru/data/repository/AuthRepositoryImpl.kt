package com.laru.data.repository

import android.content.Context
import androidx.activity.ComponentActivity
import com.laru.data.model.AuthState
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
): AuthRepository {
    override val authState = MutableStateFlow(AuthState.Waiting)

//    private val vkAuthActivity = ComponentActivity()
//    private val authLauncher = VK.login(vkAuthActivity) { result : VKAuthenticationResult ->
//        when (result) {
//            is VKAuthenticationResult.Success -> {
//                // User passed authorization
//            }
//            is VKAuthenticationResult.Failed -> {
//                // User didn't pass authorization
//            }
//        }
//    }

    override suspend fun proceedAuthorization() {
        authState.update { AuthState.SignedIn }
//        authState.map { AuthState.SignedIn }
//        authLauncher.launch(arrayListOf(VKScope.MESSAGES))
    }
}
