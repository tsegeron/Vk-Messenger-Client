package com.laru.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.laru.data.repository.ChatRepository
import dagger.assisted.AssistedInject
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel(assistedFactory = ChatViewModel.Factory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted val chatId: Int, // Long??
    private val chatRepository: ChatRepository
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(chatId: Int): ChatViewModel
    }

    init {
        viewModelScope.launch {
            chatRepository.initializeMessages(chatId)
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(factory: Factory, chatId: Int): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(chatId) as T
                }
            }
    }
}
