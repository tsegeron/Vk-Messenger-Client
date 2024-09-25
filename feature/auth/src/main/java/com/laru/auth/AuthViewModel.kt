package com.laru.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laru.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {
    val authState = authRepository.authState

    fun proceedAuthorization() = viewModelScope.launch(Dispatchers.IO) {
        authRepository.proceedAuthorization()
    }
}
