package com.laru.data.repository

import com.laru.data.model.Chat
import kotlinx.coroutines.flow.StateFlow

interface ChatsRepository {

    val chats: StateFlow<List<Chat>>
//    val chatCategories: StateFlow<List<Category>>

    suspend fun getChats()
//    suspend fun initializeDialogCategories()
//    suspend fun initializeStories()

}
