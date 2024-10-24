package com.laru.data.repository

import android.content.Context
import android.util.Log
import com.laru.common.di.ApplicationScope
import com.laru.common.extensions.TimeElapsed
import com.laru.data.R
import com.laru.data.model.Friend
import com.laru.data.model.toBirthday
import com.laru.data.model.toLastSeen
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.friends.FriendsService
import com.vk.sdk.api.friends.dto.FriendsGetFieldsResponseDto
import com.vk.sdk.api.friends.dto.FriendsGetOrderDto
import com.vk.sdk.api.users.dto.UsersFieldsDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class FriendsRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @ApplicationScope private val coroutineScope: CoroutineScope,
): FriendsRepository {
    private val _friends = MutableStateFlow(emptyList<Friend>())
    override val friends: StateFlow<List<Friend>> = _friends

    override fun getFriends() {
        val fields = listOf(
            UsersFieldsDto.PHOTO_200,   // https://dev.vk.com/ru/reference/objects/user#photo_200
            UsersFieldsDto.ONLINE,      // https://dev.vk.com/ru/reference/objects/user#online
            UsersFieldsDto.LAST_SEEN,   // https://dev.vk.com/ru/reference/objects/user#last_seen
            UsersFieldsDto.VERIFIED,    // https://dev.vk.com/ru/reference/objects/user#verified
            UsersFieldsDto.BIRTHDATE,   // https://dev.vk.com/ru/reference/objects/user#bdate
        )

//        FriendsService().friendsGetOnline()

        coroutineScope.launch(SupervisorJob() + Dispatchers.IO) {
            // order = FriendsGetOrderDto.HINTS to get friends sorted by importance
            VK.execute(FriendsService().friendsGet(fields = fields, order = FriendsGetOrderDto.HINTS), object :
                VKApiCallback<FriendsGetFieldsResponseDto> {
                override fun success(result: FriendsGetFieldsResponseDto) {
                    val friendsList = result.items
                    if (friendsList.isNotEmpty()) {
                        val friends = friendsList.map { friend ->
                            // if a user disabled showing online status then the [friend.lastSeen] comes as Null
                            // if friend.isOnline -> lastSeen = null
                            var lastSeen: Friend.LastSeenInfo? = null
                            var lastSeenFormatted: String? = null

                            if (friend.online!!.value == 0) {
                                lastSeen = friend.lastSeen?.toLastSeen()
                                lastSeenFormatted = lastSeen?.let {
                                    when (it.timeElapsed) {
                                        is TimeElapsed.OnDate ->
                                            appContext.getString(
                                                R.string.last_seen_on_date,
                                                it.timeElapsed.date
                                            )

                                        is TimeElapsed.OnDay ->
                                            appContext.getString(
                                                R.string.last_seen_on_day,
                                                it.timeElapsed.day
                                            )

                                        is TimeElapsed.AtTime ->
                                            appContext.getString(
                                                R.string.last_seen_at_time,
                                                it.timeElapsed.time
                                            )

                                        is TimeElapsed.MinutesAgo ->
                                            appContext.getString(
                                                R.string.last_seen_minutes_ago,
                                                it.timeElapsed.minutes
                                            )

                                        TimeElapsed.Recently ->
                                            appContext.getString(R.string.last_seen_recently)
                                    }
                                } ?: appContext.getString(R.string.last_seen_long_ago)
                            }

                            Friend(
                                id = friend.id.value,
                                firstName = friend.firstName ?: "",
                                lastName = friend.lastName ?: "",
                                photo = friend.photo200 ?: "",
                                birthday = friend.bdate?.toBirthday(),
                                isOnline = friend.online?.value == 1,
                                isOnlineMobile = friend.onlineMobile?.value == 1,
                                isVerifiedUser = friend.verified?.value == 1,
                                lastSeen = lastSeen,
                                lastSeenFormatted = lastSeenFormatted
                            )
                        }

                        _friends.update { friends }
                    }
                }

                override fun fail(error: Exception) {
                    Log.e(TAG, error.toString())
                }
            })
        }
    }

    companion object {
        const val TAG = "FriendsRepositoryImpl"
    }
}
