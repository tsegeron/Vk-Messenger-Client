package com.laru.friends.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.common.preview.ThemePreviews
import com.laru.data.model.Friend
import com.laru.ui.composable.ProfilePhoto


@Composable
fun FriendsListItemRow(
    friend: Friend,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
    ) {
        ProfilePhoto(imageModel = null, imageSize = 40.dp)

        Column {
            Text(text = friend.name)
//            Text(text = "online")
//            Text(text = "last seen just now")
//            Text(text = "last seen 5 minutes ago")
//            Text(text = "last seen 1 hour ago")
            Text(text = "last seen yesterday at 11:53")
//            Text(text = "last seen 23.11.24")
        }
    }
}


@ThemePreviews
@Composable
fun FriendsListItemRowPreview(modifier: Modifier = Modifier) {
    VKMessengerTheme {
        FriendsListItemRow(
            friend = Friend(0, null, "Aygiz")
        )
    }
}
