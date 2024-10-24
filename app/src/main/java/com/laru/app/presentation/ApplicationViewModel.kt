package com.laru.app.presentation

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laru.data.model.AuthState
import com.laru.data.repository.AuthRepository
import com.laru.settings.data.entity.AccessTokenEntity
import com.laru.settings.data.repo.AccessTokenRepo
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
//    private val accessTokenRepo: AccessTokenRepo,
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (VK.isLoggedIn())
                authRepository.loginSucceeded()
        }
    }


    fun loginSucceeded() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginSucceeded()
        }
    }

    fun loginFailed() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginFailed()
        }
    }








    val appAuthState = authRepository.authState.map {
        when (it) {
            AuthState.SignedIn -> AppStartState.Authorized
            else -> AppStartState.Unauthorized
        }
    }.stateIn(
        scope = viewModelScope,
//        started = SharingStartedDefaultTimeout,
        started = SharingStarted.Eagerly,
        initialValue = AppStartState.Unauthorized
    )


    companion object {
        val SharingStartedDefaultTimeout =
            SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds)
    }
}



enum class AppStartState {
    Authorized,
    Unauthorized
}
