package com.laru.auth.model

import androidx.annotation.StringRes
import com.laru.auth.R
import com.laru.data.model.SignInResponse

sealed interface AuthUiState {
    @get:StringRes val titleResId: Int
    @get:StringRes val subtitleResId: Int
    val loading: Boolean
    val errorMessage: String? // change

    data class Start(
        override val loading: Boolean = false,
        override val errorMessage: String? = null,
        override val titleResId: Int = R.string.sign_in_title,
        override val subtitleResId: Int = R.string.sign_in_subtitle
    ): AuthUiState

    sealed interface Login: AuthUiState {
        val signInResponse: SignInResponse // to implement?

        data class Phone(
            override val titleResId: Int, // Login to vk
            override val subtitleResId: Int, // Please enter ...
            override val signInResponse: SignInResponse,
            override val errorMessage: String?,
            override val loading: Boolean = false
        ): Login

        data class Email(
            override val titleResId: Int, // Login to vk
            override val subtitleResId: Int, // Please enter ...
            override val signInResponse: SignInResponse,
            override val errorMessage: String?,
            override val loading: Boolean = false
        ): Login

        data class Password(
            override val titleResId: Int, // ??
            override val subtitleResId: Int, // Please enter ...
            override val signInResponse: SignInResponse,
            override val errorMessage: String?,
            override val loading: Boolean
        ): Login
    }

    // Forgot password???
}
