package com.laru.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laru.data.model.AuthState
import com.laru.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class ApplicationViewModel @Inject constructor(
    authRepository: AuthRepository
): ViewModel() {

    val appAuthState = authRepository.authState.map {
        when (it) {
            AuthState.SignedIn -> AppStartState.Authorized
            else -> AppStartState.Unauthorized
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStartedDefaultTimeout,
//        started = SharingStarted.Eagerly,
        initialValue = AppStartState.Unauthorized
    )


    companion object {
        val SharingStartedDefaultTimeout = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds)
    }
}


enum class AppStartState {
    Authorized,
    Unauthorized
}
