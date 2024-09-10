package com.laru.app.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.laru.app.presentation.ChatsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.common.preview.ThemePreviews


@Composable
fun ChatsScreen(
//    viewModel: ChatsViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            ChatsTopAppBar()
        }
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            (1..15).forEach {
                item {
                    Row(Modifier.height(60.dp).fillMaxWidth()) {
                        Text(text = "$it")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsTopAppBar(
    modifier : Modifier = Modifier
) {
    LargeTopAppBar(
        modifier = modifier,
        title = {
            Text(text = "Chats")
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        },
//        collapsedHeight = ,
//        expandedHeight = ,
//        windowInsets = ,
//        colors = ,
//        scrollBehavior = TopAppBarScrollBehavior
    )
}



@ThemePreviews
@Composable
fun VKMTopAppBarPreview() {
    VKMessengerTheme {
        ChatsTopAppBar()
    }
}
