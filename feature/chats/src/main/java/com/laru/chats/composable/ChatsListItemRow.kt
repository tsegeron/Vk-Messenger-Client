package com.laru.chats.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.chats.R
import com.laru.chats.model.ChatsListItem
import com.laru.common.extensions.toDatetime
import com.laru.common.preview.ThemePreviews
import com.laru.ui.model.Paddings
import com.laru.ui.model.Spacers


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatsListItemRow(
    chat: ChatsListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier
            .padding(vertical = Paddings.extraSmall, horizontal = Paddings.small)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        ProfilePhoto(chat.profileImage)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ChatTitle(
                title = chat.title,
                isMuted = chat.isMuted,
                lastMsgTime = chat.lastMsgTime.toDatetime(),
            )
            ChatContent(
                lastMsgSneakPeak = chat.lastMsg,
                unreadMsgCount = chat.unreadCount,
            )
        }
    }
}

@Composable
private fun ProfilePhoto(
    profileImage: Any?
) {
    Icon(
        imageVector = Icons.Filled.Person,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
//        contentScale = ContentScale.Crop,
        tint = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun ChatTitle(
    title: String,
    isMuted: Boolean,
    lastMsgTime: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f, fill = false),
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
            )

            if (isMuted) {
                Spacer(modifier = Modifier.width(Spacers.extraSmall))
                Icon(
                    painter = painterResource(id = R.drawable.volume_off),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        Spacer(modifier = Modifier.width(Spacers.extraLarge))
        Text(
            text = lastMsgTime,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun ChatContent(
    lastMsgSneakPeak: String,
    unreadMsgCount: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = lastMsgSneakPeak,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.width(Spacers.small))

        MsgCountBadge(unreadMsgCount)
    }
}


/*
    1. Empty – Not pinned, no unread messages
    2. Pinned – Pinned, no unread messages
    3. Count – Unread messages count
 */
@Composable
private fun MsgCountBadge(
    msgCount: Int,
) {
    Badge(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
    ) {
        Text(
            text = msgCount.toString(),
        )
    }
}

@Composable
@ThemePreviews
private fun ChatsRowPreview() {
    VKMessengerTheme {
        Surface(color = MaterialTheme.colorScheme.secondaryContainer) {
            Column {
                ChatsListItemRow(
                    chat = ChatsListItem.mockDataShort,
                    onClick = {},
                )
                HorizontalDivider()

                ChatsListItemRow(
                    chat = ChatsListItem.mockDataShortMuted,
                    onClick = {},
                )
                HorizontalDivider()

                ChatsListItemRow(
                    chat = ChatsListItem.mockDataLong,
                    onClick = {},
                )
                HorizontalDivider()

                ChatsListItemRow(
                    chat = ChatsListItem.mockDataLongMuted,
                    onClick = {},
                )
                HorizontalDivider()
            }
        }
    }
}
