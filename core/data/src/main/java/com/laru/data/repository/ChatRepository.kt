package com.laru.data.repository


interface ChatRepository {

    suspend fun initializeMessages(chatId: Long)
}
