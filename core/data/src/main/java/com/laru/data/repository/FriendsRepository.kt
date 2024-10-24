package com.laru.data.repository

import com.laru.data.model.Friend
import kotlinx.coroutines.flow.StateFlow


interface FriendsRepository {
    val friends: StateFlow<List<Friend>>

    fun getFriends()
}
