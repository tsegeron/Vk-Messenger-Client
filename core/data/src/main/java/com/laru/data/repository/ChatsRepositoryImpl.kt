package com.laru.data.repository

import com.laru.data.model.Chat
import com.vk.api.sdk.VK
import com.vk.sdk.api.messages.MessagesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class ChatsRepositoryImpl @Inject constructor(

): ChatsRepository {
    override val chats: StateFlow<List<Chat>> = flow {
        emit(List(20) { Chat.mockDataLongMuted })
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(5_000),
//        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    override suspend fun getChats() {
//        VK.execute(MessagesService().messagesGet)
    }
}
