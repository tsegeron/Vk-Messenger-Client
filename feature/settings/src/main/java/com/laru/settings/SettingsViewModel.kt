package com.laru.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laru.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.logout()
        }
    }
}
