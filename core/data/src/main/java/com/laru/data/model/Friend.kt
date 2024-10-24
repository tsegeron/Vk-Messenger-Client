package com.laru.data.model

import com.laru.common.extensions.TimeElapsed
import com.laru.common.extensions.getDaysUntilBirthday
import com.laru.common.extensions.toTimeElapsed
import com.vk.sdk.api.users.dto.UsersLastSeenDto

data class Friend(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val photo: String, // specify size?
    val birthday: Birthday? = null,
    val lastSeen: LastSeenInfo? = null, // null if isOnline
    val lastSeenFormatted: String? = null, // null if lastSeen == null
    val isOnline: Boolean,
    val isOnlineMobile: Boolean,
    val isVerifiedUser: Boolean,
) {
    data class Birthday(
        val date: String,
        val daysTillBirthday: Int
    )

    data class LastSeenInfo(
        val time: Int? = null,
        val device: Device? = null,
        val timeElapsed: TimeElapsed,
    ) {
        enum class Device {
            Mobile, Desktop,
        }
    }
}

/**
 * Narrows a variety of devices down to 2 options
 *
 *
 * https://dev.vk.com/ru/reference/objects/user#last_seen
 * Initial options as Int from the server:
 *      1. mobile version
 *      2. iPhone
 *      3. iPad
 *      4. Android
 *      5. Windows Phone
 *      6. Windows
 *      7. Webpage version
 */
fun String.toBirthday() = Friend.Birthday(
    date = this,
    daysTillBirthday = this.getDaysUntilBirthday()
)

fun UsersLastSeenDto.toLastSeen() = Friend.LastSeenInfo(
    time = time!!,
    timeElapsed = time!!.toTimeElapsed(),
    device = when(platform) {
        6, 7 -> Friend.LastSeenInfo.Device.Desktop
        else -> Friend.LastSeenInfo.Device.Mobile
    },
)
