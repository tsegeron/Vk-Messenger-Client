package com.laru.friends.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.common.preview.ThemePreviews
import com.laru.data.model.Friend
import com.laru.friends.R
import com.laru.ui.composable.ProfilePhoto
import com.laru.ui.extensions.shimmerEffect
import com.laru.ui.model.Paddings
import com.laru.ui.model.Sizes
import com.laru.ui.model.Spacers


@Composable
fun FriendsListItemRow(
    friend: Friend,
    showBirthdate: Boolean,
    onFriendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onFriendClick)
    ) {
        ProfilePhoto(imageModel = friend.photo, imageSize = 40.dp)

        Spacer(Modifier.width(Spacers.medium))

        Column {
            Row {
                Text(text = friend.firstName)
                Spacer(Modifier.width(Spacers.extraSmall))
                Text(text = friend.lastName)

                if (friend.isVerifiedUser) {
                    Spacer(Modifier.width(Spacers.extraSmall))
                    Icon( // TODO change verified user Icon
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(Sizes.iconSmall)
                    )
                }
            }

            if (showBirthdate && friend.birthday != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = friend.birthday!!.date)
                    Spacer(Modifier.width(Spacers.extraSmall))
                    val text = when (friend.birthday!!.daysTillBirthday) {
                        0 -> stringResource(R.string.today)
                        1 -> stringResource(R.string.tomorrow)
                        else -> stringResource(R.string.days_left, friend.birthday!!.daysTillBirthday)
                    }
                    Text(text = text)
                }
            } else {
                if (friend.isOnline) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.online))

                        if (friend.isOnlineMobile) {
                            Spacer(Modifier.width(Spacers.extraSmall))
                            Icon(
                                painter = painterResource(R.drawable.smartphone_white),
                                contentDescription = null,
                                modifier = Modifier.size(Sizes.iconSmall) // Sizes.iconDefault
                            )
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // if !friend.isOnline then friend.lastSeenFormatted can not be null
                        Text(
                            text = friend.lastSeenFormatted!!,
    //                        style = MaterialTheme.typography.bodySmall
                        )

                        // if !friend.isOnline then friend.lastSeen can be null
                        // https://dev.vk.com/ru/reference/objects/user#last_seen
                        if (friend.lastSeen != null && friend.lastSeen!!.device != null
                            && friend.lastSeen!!.device == Friend.LastSeenInfo.Device.Mobile) {
                            Spacer(Modifier.width(Spacers.extraSmall))
                            Icon(
                                painter = painterResource(R.drawable.smartphone_white),
                                contentDescription = null,
                                modifier = Modifier.size(Sizes.iconSmall)
                            )
                        }
                    }
                }
            }


            HorizontalDivider(
                modifier = Modifier.padding(top = Paddings.extraSmall),
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun LoadingFriendsListItemRow(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Box(
            Modifier
                .padding(bottom = Paddings.small)
                .size(Sizes.photoSmall)
                .shimmerEffect(CircleShape)
        )
        Spacer(Modifier.width(Spacers.medium))
        Column {
            Box(
                Modifier
                    .height(16.dp)
                    .width(160.dp)
                    .shimmerEffect(RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.height(Spacers.small))

            Box(
                Modifier
                    .height(16.dp)
                    .width(240.dp)
                    .shimmerEffect(RoundedCornerShape(4.dp))
            )

            HorizontalDivider(
                modifier = Modifier.padding(top = Paddings.small),
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        }
    }
}


@ThemePreviews
@Composable
private fun FriendsListItemRowPreview(modifier: Modifier = Modifier) {
    val mockFriend = Friend(
        id = 1,
        firstName = "Rayan",
        lastName = "Gosling",
        photo = "",
        isOnline = true,
        isOnlineMobile = true,
        isVerifiedUser = true
    )
    VKMessengerTheme {
        Column(
            Modifier.padding(start = 8.dp)
        ) {
            FriendsListItemRow(
                friend = mockFriend,
                showBirthdate = false,
                onFriendClick = {},
            )
            FriendsListItemRow(
                friend = mockFriend,
                showBirthdate = false,
                onFriendClick = {},
            )
            LoadingFriendsListItemRow()
            LoadingFriendsListItemRow()
        }
    }
}
