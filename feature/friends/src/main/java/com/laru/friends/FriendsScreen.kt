package com.laru.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.common.preview.ThemePreviews
import com.laru.data.model.Friend
import com.laru.friends.composable.FriendsListItemRow
import com.laru.friends.composable.LoadingFriendsListItemRow
import com.laru.friends.model.FriendsUiState
import com.laru.ui.animation.AnchoredLazyColumnNestedScrollConnection
import com.laru.ui.animation.textFieldAnchoredDraggableState
import com.laru.ui.composable.BasicInputTextField
import com.laru.ui.composable.ToTopAnimatedFloatingActionButton
import com.laru.ui.extensions.isAtTop
import com.laru.ui.model.Paddings
import kotlinx.coroutines.launch
import com.laru.ui.R as uiR


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FriendsScreen(
    onFriendClick: (Long) -> Unit,
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val isAtTop = remember { derivedStateOf { listState.isAtTop } }
    val isToTopIconVisible = remember { derivedStateOf { listState.firstVisibleItemIndex > 4 } }

    val anchoredDraggableState = remember { textFieldAnchoredDraggableState(density) }
    val nestedScrollConnection = remember {
        AnchoredLazyColumnNestedScrollConnection(anchoredDraggableState, isAtTop)
    }

    var showSortOptions by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.consumeWindowInsets(WindowInsets.navigationBars),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Friends") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                navigationIcon = {
                    IconButton(onClick = { /*TODO */ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = { showSortOptions = true }) {
                        Text(
                            text = stringResource(R.string.sort),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp
                        )
                    }
                    DropdownMenu(
                        expanded = showSortOptions,
                        onDismissRequest = { showSortOptions = false },
                        shape = RoundedCornerShape(12.dp),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.85f),
                        offset = DpOffset((-12).dp, 8.dp)
                    ) {
                        FriendsUiState.SortType.entries.onEach { sortType ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(sortType.sortTypeResId),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                onClick = {
                                    viewModel.onSortTypeChange(sortType)
                                    showSortOptions = false
                                },
                                modifier = Modifier.height(40.dp),
                                trailingIcon = viewModel.getSortTypeTrailingIcon(sortType)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ToTopAnimatedFloatingActionButton(
                isVisible = isToTopIconVisible.value,
                onClick = { coroutineScope.launch { listState.animateScrollToItem(0) } }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.friendsUiState.collectAsState()
        val focusManager = LocalFocusManager.current

        Column(Modifier.padding(innerPadding)) {
            val textFieldHeight = with(density) { anchoredDraggableState.requireOffset().toDp() }
            BasicInputTextField(
                value = uiState.searchPrompt,
                onValueChange = viewModel::onTextFieldValueChange,
                modifier = Modifier
                    .padding(
                        horizontal = Paddings.medium,
                        vertical = textFieldHeight.coerceIn(Paddings.default, Paddings.small)
                    )
                    .fillMaxWidth()
                    .height((textFieldHeight - Paddings.small * 2).coerceIn(0.dp, 40.dp)),
                contentModifier = Modifier.alpha((textFieldHeight.value - 36f).coerceIn(0f, 20f) / 20), // fadein/fadeout effect
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                placeholderText = stringResource(uiR.string.placeholder_search),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
            )

            FriendsScreenContent(
                friendsList = when {
                    uiState.friendsList.isEmpty() && uiState.searchPrompt.text.isEmpty() -> null
                    uiState.searchPrompt.text.isEmpty() -> uiState.friendsListSorted
                    else -> uiState.friendsListOnSearch
                },
//                birthdaysList = uiState.closestBirthdays,
                showBirthdate = uiState.sortType == FriendsUiState.SortType.Birthday,
                onFriendClick = onFriendClick,
                nestedScrollConnection = nestedScrollConnection,
                listState = listState,
                modifier = Modifier.padding(start = Paddings.medium)
            )
        }
    }
}

// TODO add scroll to top when Friends button was pressed again
// TODO add global search on Friends button long press
@Composable
private fun FriendsScreenContent(
    friendsList: List<Friend>?,
//    birthdaysList: List<Friend>,
    showBirthdate: Boolean,
    onFriendClick: (Long) -> Unit,
    nestedScrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    if (friendsList == null) {
        LazyColumn(modifier.padding(top = Paddings.extraSmall), userScrollEnabled = false) {
            items(20) {
                LoadingFriendsListItemRow()
            }
        }
    } else if (friendsList.isEmpty()) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(stringResource(R.string.no_results))
        }
    } else {
        // TODO ModalDrawerSheet to profile and chat!!
        // TODO add a row with a birthday if its today/tomorrow!!
//        val tomorrowBirthdayNames = birthdaysList
//            .filter { it.birthday!!.daysTillBirthday == 1 }
//            .map(Friend::firstName)
//            .joinToString(", ")

        LazyColumn(
            modifier = modifier.nestedScroll(nestedScrollConnection),
            state = listState
        ) {
//            item() {
//                Text
//            }

            items(friendsList, key = Friend::id) { friend ->
                FriendsListItemRow(
                    friend = friend,
                    showBirthdate = showBirthdate,
                    onFriendClick = { onFriendClick(friend.id) },
                )
            }
        }
    }
}


@ThemePreviews
@Composable
private fun FriendsScreenContentPreview() {
    val mock = List(20) {
        Friend(
            id = 1,
            firstName = "Rayan",
            lastName = "Gosling",
            photo = "",
            isOnline = true,
            isOnlineMobile = true,
            isVerifiedUser = true
        )
    }

    VKMessengerTheme {
        FriendsScreenContent(
            friendsList = mock,
            showBirthdate = false,
            onFriendClick = { },
            nestedScrollConnection = object : NestedScrollConnection {},
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
