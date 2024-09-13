package com.laru.data.repository

import android.content.Context
import com.laru.data.model.AuthState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
): AuthRepository {
    override val authState: StateFlow<AuthState>
        get() = TODO("Not yet implemented")
}
