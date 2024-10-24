package com.laru.data.repository

import com.laru.data.model.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>

    suspend fun loginSucceeded()
    suspend fun loginFailed()
    suspend fun logout()
}
