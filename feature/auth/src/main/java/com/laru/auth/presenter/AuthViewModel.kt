package com.laru.auth.presenter

import androidx.lifecycle.ViewModel
import com.laru.auth.model.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(

): ViewModel() {
    private val _authUiState = MutableStateFlow(AuthUiState.Start())
    val authUiState = _authUiState.asStateFlow()


}
