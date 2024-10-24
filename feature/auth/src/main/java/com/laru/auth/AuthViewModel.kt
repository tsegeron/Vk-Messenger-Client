package com.laru.auth

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laru.data.model.AuthState
import com.laru.data.repository.AuthRepository
import com.laru.datastore.data.repository.AuthTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {
    val authState = authRepository.authState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AuthState.Waiting
    )
    // TODO autologin if refresh token is not expired (for now works with access token)

//    fun proceedAuthorization() = viewModelScope.launch(Dispatchers.IO) {
//        authRepository.authorize()
//    }
}
