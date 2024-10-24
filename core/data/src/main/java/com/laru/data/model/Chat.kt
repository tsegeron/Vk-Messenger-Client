package com.laru.data.model


data class Chat(
    val id: Long,
    val title: String,
    val isMuted: Boolean,
    val lastMsg: String,
    val lastMsgTime: Int,
    val unreadCount: Int,
    val profileImage: Any?
) {
    companion object {
        val mockDataLongMuted = Chat(
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
