package com.laru.chats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.chats.composable.ChatsListItemRow
import com.laru.chats.model.ChatsListItem
import com.laru.common.preview.ThemePreviews
import com.laru.data.model.ChatCategory
import com.laru.ui.animation.AnchoredPagerNestedScrollConnection
import com.laru.ui.animation.textFieldAnchoredDraggableState
import com.laru.ui.composable.BasicInputTextField
import com.laru.ui.composable.ScrollableTabRow
import com.laru.ui.composable.ToTopAnimatedFloatingActionButton
import com.laru.ui.extensions.isAtTop
import com.laru.ui.model.AnchorState
import com.laru.ui.model.Paddings
import kotlinx.coroutines.launch
import com.laru.ui.R as uiR


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatsScreen(
    onChatClick: (Long) -> Unit,
    chatsViewModel: ChatsViewModel = hiltViewModel()
) {
    val chats by chatsViewModel.chats.collectAsState()
    val categories = chatsViewModel.chatCategories

    val pagerState = rememberPagerState { categories.size }
    val listsStates = List(categories.size) { rememberLazyListState() }
    val isToTopIconVisible = listsStates.map { listState -> remember {
        derivedStateOf { listState.firstVisibleItemIndex > 4 }
    } }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "NeVk") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ToTopAnimatedFloatingActionButton(
                isVisible = isToTopIconVisible[pagerState.currentPage].value,
                onClick = { coroutineScope.launch {
                    listsStates[pagerState.currentPage].animateScrollToItem(0)
                } }
            )
        }
    ) { innerPadding ->
        var textFieldValue: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

        val density = LocalDensity.current
        val anchoredDraggableState = remember { textFieldAnchoredDraggableState(density) }
        val isAtTop = listsStates.map { listState -> remember {
            derivedStateOf { listState.isAtTop }
        } }

        val nestedScrollConnection = remember {
            AnchoredPagerNestedScrollConnection(anchoredDraggableState, isAtTop, pagerState)
        }

        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val textFieldHeight = with(density) { anchoredDraggableState.requireOffset().toDp() }
            BasicInputTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .padding(
                        horizontal = Paddings.medium,
                        vertical = textFieldHeight.coerceIn(Paddings.default, Paddings.small)
                    )
                    .fillMaxWidth()
                    .height((textFieldHeight - Paddings.small * 2).coerceIn(0.dp, 40.dp)),
                // alpha = (0..56) - 36  --->  -36..20  -->  0..20  ->  0..1f  ==>  36..56.dp == 0..1f
                contentModifier = Modifier.alpha((textFieldHeight.value - 36f).coerceIn(0f, 20f) / 20), // fadein/fadeout effect
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                placeholderText = stringResource(uiR.string.placeholder_search),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
            )

            ScrollableTabRow(
                tabItems = chatsViewModel.chatCategories,
                pagerState = pagerState,
                modifier = Modifier
            )

            HorizontalPager(
                state = pagerState,
//                flingBehavior = PagerDefaults.flingBehavior(
//                    state = pagerState,
//                    snapAnimationSpec = tween(250, easing = FastOutSlowInEasing)
//                ),
                snapPosition = SnapPosition.Center,
                pageNestedScrollConnection = nestedScrollConnection
            ) { index ->
                // TODO: filter chats corresponding to category from all chats
                when (categories[index]) {
                    ChatCategory.All -> {
                        MockAllChatsColumn(listsStates[0], chats, onChatClick)
                    }

                    ChatCategory.Personal -> {
                        MockAllChatsColumn(listsStates[1], chats.slice(0..14), onChatClick)
                    }

                    ChatCategory.Channels -> {
                        MockAllChatsColumn(listsStates[2], chats.slice(0..4), onChatClick)
                    }

                    ChatCategory.Bots -> {
                        MockAllChatsColumn(listsStates[3], chats.slice(0..2), onChatClick)
                    }
                }
            }
        }
    }
}

@Composable
fun MockAllChatsColumn(
    state: LazyListState,
    chats: List<ChatsListItem>,
    onChatClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        LazyColumn(
            state = state,
            modifier = Modifier
        ) {
            itemsIndexed(chats) { index, chat ->
                ChatsListItemRow(
                    chat = chat,
                    onClick = { onChatClick(index.toLong()) })
            }
        }
    }
}

@Composable
@ThemePreviews
fun ChatsScreenPreview() {
    VKMessengerTheme {
        ChatsScreen({})
    }
}
