package com.laru.chats.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.chats.ui.composable.ChatsListItemRow
import com.laru.chats.ui.model.ChatsListItem
import com.laru.common.preview.ThemePreviews
import com.laru.ui.composable.BasicInputTextField
import com.laru.ui.composable.TabItem
import com.laru.ui.composable.VKMTabRow
import com.laru.ui.composable.bottombar.VKMBottomAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    chats: List<ChatsListItem> // maybe null/empty
) {
//    val scrollBehavior
    val pagerState = rememberPagerState {
        TabItem.entries.size
    }
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "NeVk") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Blue
                )
            )
        },
        bottomBar = { VKMBottomAppBar({}, {}, {}) },
        snackbarHost = {

        },
        floatingActionButton = {
            IconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
            }
        }
    ) { innerPadding ->
        var texFieldValue: TextFieldValue by remember {
            mutableStateOf(TextFieldValue(""))
        }

        Column(Modifier.padding(innerPadding)) {
            BasicInputTextField(
                value = texFieldValue,
                onValueChange = { texFieldValue = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                placeholderText = "Search",
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
            )

            VKMTabRow(
                tabItems = TabItem.entries,
                pagerState = pagerState,
                modifier = Modifier
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                // TODO: filter chats corresponding to category from all chats
                when(TabItem.entries[index]) {
                    TabItem.All -> { MockAllChatsColumn(chats) }
                    TabItem.Personal -> { MockAllChatsColumn(chats.slice(0..9)) }
                    TabItem.Channels -> { MockBotsChatsColumn(chats.slice(0..4)) }
                    TabItem.Bots -> { MockAllChatsColumn(chats.slice(0..2)) }
                }
            }
        }
    }
}

@Composable
fun MockAllChatsColumn(
    chats: List<ChatsListItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(chats) { chat ->
            ChatsListItemRow(
                chat = chat,
                onClick = { /*TODO*/ })
        }
    }
}

@Composable
fun MockBotsChatsColumn(
    chats: List<ChatsListItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(chats) { chat ->
            ChatsListItemRow(
                chat = chat,
                onClick = { /*TODO*/ })
        }
    }
}

@Composable
@ThemePreviews
fun ChatsScreenPreview() {
    val mockData = listOf(
        ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
        ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
        ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
    )
    VKMessengerTheme {
        ChatsScreen(mockData)
    }
}
