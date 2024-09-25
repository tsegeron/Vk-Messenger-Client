package com.laru.chats

import androidx.compose.animation.AnimatedVisibility
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
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.chats.composable.ChatsListItemRow
import com.laru.chats.model.ChatsListItem
import com.laru.common.preview.ThemePreviews
import com.laru.ui.composable.BasicInputTextField
import com.laru.ui.composable.ScrollableTabRow
import com.laru.ui.composable.TabItem
import com.laru.ui.model.Paddings
import kotlinx.coroutines.launch


internal enum class AnchorState { Start, End }


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatsScreen(
//    chats: List<ChatsListItem> // maybe null/empty
    onChatClick: (Int) -> Unit,
) {
    val mockData = listOf(
        ChatsListItem.mockDataShort,
        ChatsListItem.mockDataLong,
        ChatsListItem.mockDataShortMuted,
        ChatsListItem.mockDataLongMuted,
        ChatsListItem.mockDataShort,
        ChatsListItem.mockDataLong,
        ChatsListItem.mockDataShortMuted,
        ChatsListItem.mockDataLongMuted,
        ChatsListItem.mockDataShort,
        ChatsListItem.mockDataLong,
        ChatsListItem.mockDataShortMuted,
        ChatsListItem.mockDataLongMuted,
        ChatsListItem.mockDataShort,
        ChatsListItem.mockDataLong,
        ChatsListItem.mockDataShortMuted,
        ChatsListItem.mockDataLongMuted,
        ChatsListItem.mockDataShort,
        ChatsListItem.mockDataLong,
        ChatsListItem.mockDataShortMuted,
        ChatsListItem.mockDataLongMuted,
    )

    val pagerState = rememberPagerState { TabItem.entries.size }
    val listsStates = List(4) { rememberLazyListState() }
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
            AnimatedVisibility(
                visible = isToTopIconVisible[pagerState.currentPage].value,
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it / 2 })
            ) {
                IconButton(
                    onClick = { coroutineScope.launch {
                        listsStates[pagerState.currentPage].animateScrollToItem(0)
                    } },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        var textFieldValue: TextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

        val density = LocalDensity.current
        val anchoredDraggableState = remember {
            AnchoredDraggableState(
                initialValue = AnchorState.Start,
                anchors = DraggableAnchors {
                    AnchorState.Start at 0f
                    AnchorState.End at with(density) { 56.dp.toPx() } // max textField height
                },
                positionalThreshold = { distance: Float -> distance * 0.4f },
                velocityThreshold = { Int.MAX_VALUE.toFloat() },
                snapAnimationSpec = tween(),
                decayAnimationSpec = splineBasedDecay(density),
            )
        }
        val isAtTop = listsStates.map { listState -> remember {
            derivedStateOf { listState.isAtTop }
        } }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                private fun Float.toYOffset() = Offset(0f, this)

                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    val delta = available.y
                    if (delta > 0 && isAtTop[pagerState.currentPage].value) {
                        anchoredDraggableState.dispatchRawDelta(delta)
                        return available
                    }
                    if (delta < 0 && (isAtTop[pagerState.currentPage].value || anchoredDraggableState.currentValue == AnchorState.End)) {
                        val consumed = anchoredDraggableState.dispatchRawDelta(delta)
                        return consumed.toYOffset()
                    }

                    return super.onPreScroll(available, source)
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    val delta = available.y

                    if (delta > 0 && isAtTop[pagerState.currentPage].value) {
                        return anchoredDraggableState.dispatchRawDelta(delta).toYOffset()
                    }
                    return super.onPostScroll(consumed, available, source)
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity,
                ): Velocity {
                    anchoredDraggableState.settle(available.y)
                    return super.onPostFling(consumed, available)
                }
            }
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
                placeholderText = stringResource(R.string.placeholder_search),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
            )

            ScrollableTabRow(
                tabItems = TabItem.entries,
                pagerState = pagerState,
                modifier = Modifier
            )

            HorizontalPager(
                state = pagerState,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    snapAnimationSpec = tween(250, easing = LinearEasing)
                ),
                snapPosition = SnapPosition.Center,
                pageNestedScrollConnection = nestedScrollConnection
            ) { index ->
                // TODO: filter chats corresponding to category from all chats
                when (TabItem.entries[index]) {
                    TabItem.All -> {
                        MockAllChatsColumn(listsStates[0], mockData, onChatClick)
                    }

                    TabItem.Personal -> {
                        MockAllChatsColumn(listsStates[1], mockData.slice(0..14), onChatClick)
                    }

                    TabItem.Channels -> {
                        MockAllChatsColumn(listsStates[2], mockData.slice(0..4), onChatClick)
                    }

                    TabItem.Bots -> {
                        MockAllChatsColumn(listsStates[3], mockData.slice(0..2), onChatClick)
                    }
                }
            }
        }
    }
}

val LazyListState.isAtTop: Boolean
    get() = firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0

@Composable
fun MockAllChatsColumn(
    state: LazyListState,
    chats: List<ChatsListItem>,
    onChatClick: (Int) -> Unit,
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
                    onClick = { onChatClick(index) })
            }
        }
    }
}

@Composable
@ThemePreviews
fun ChatsScreenPreview() {
//    val mockData = listOf(
//        ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
//        ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
//        ChatsListItem.mockDataShort, ChatsListItem.mockDataLong, ChatsListItem.mockDataShortMuted, ChatsListItem.mockDataLongMuted,
//    )
    VKMessengerTheme {
        ChatsScreen({})
    }
}
