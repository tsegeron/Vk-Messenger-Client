package com.laru.chats.model

data class ChatsListItem(
    val id: Long,
    val title: String,
    val isMuted: Boolean,
    val lastMsg: String,
    val lastMsgTime: Int,
    val unreadCount: Int,
    val profileImage: Any?
) {
    companion object {
        val mockDataShort = ChatsListItem(
            id = 0,
            title = "Chats Row Title",
            isMuted = false,
            lastMsg = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus",
            lastMsgTime = 1_725_893_133,
            unreadCount = 256,
            profileImage = null,
        )
        val mockDataShortMuted = ChatsListItem(
            id = 0,
            title = "Chats Row Title",
            isMuted = true,
            lastMsg = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus",
            lastMsgTime = 1_725_802_733,
            unreadCount = 256,
            profileImage = null,
        )
        val mockDataLong = ChatsListItem(
            id = 0,
            title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus",
            isMuted = false,
            lastMsg = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus luctus, nisl ut lobortis feugiat, nibh mi ultrices risus, eu vulputate elit odio sit amet libero.",
            lastMsgTime = 1_725_893_133,
            unreadCount = 256,
            profileImage = null,
        )
        val mockDataLongMuted = ChatsListItem(
            id = 0,
            title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus",
            isMuted = true,
            lastMsg = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus luctus, nisl ut lobortis feugiat, nibh mi ultrices risus, eu vulputate elit odio sit amet libero.",
            lastMsgTime = 1_725_169_933,
            unreadCount = 256,
            profileImage = null,
        )
    }
}
