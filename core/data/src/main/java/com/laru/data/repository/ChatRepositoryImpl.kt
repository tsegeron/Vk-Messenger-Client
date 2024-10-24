package com.laru.data.repository

import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(

): ChatRepository {
    override suspend fun initializeMessages(chatId: Long) {

    }
}
