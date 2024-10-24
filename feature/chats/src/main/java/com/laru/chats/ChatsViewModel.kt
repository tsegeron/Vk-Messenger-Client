package com.laru.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laru.chats.model.toChatsListItem
import com.laru.data.model.ChatCategory
import com.laru.data.repository.ChatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatsRepository: ChatsRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            chatsRepository.getChats()
        }
    }

    val chatCategories = ChatCategory.entries

    val chats = chatsRepository.chats.map { allChats ->
        allChats.map { it.toChatsListItem() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
//        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

}
