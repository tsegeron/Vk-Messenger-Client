package com.laru.friends

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laru.data.NetworkConnectionManager
import com.laru.data.model.Friend
import com.laru.data.repository.FriendsRepository
import com.laru.friends.model.FriendsUiState
import com.laru.friends.model.FriendsUiState.SortType
import com.laru.ui.model.Sizes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val friendsRepository: FriendsRepository,
    networkConnectionManager: NetworkConnectionManager,
): ViewModel() {

    private val _friendsUiState = MutableStateFlow(FriendsUiState())
    val friendsUiState = _friendsUiState

    private var isFriendsListReversed: Boolean = false

    init {
        networkConnectionManager.isNetworkConnectedFlow.onEach { isAvailable ->
            if (isAvailable) {
                friendsRepository.getFriends()
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            friendsRepository.friends.collect { friendsList ->
                _friendsUiState.update { currentState ->
                    currentState.copy(
                        friendsList = friendsList,
                        friendsListSorted = getSortedFriendsList(friendsList, currentState.sortType)
                    )
                }
            }
        }
    }

    // TODO add auto data refreshing
//    fun onRefresh() {
//        friendsRepository.getFriends()
//    }

    fun onTextFieldValueChange(newPrompt: TextFieldValue) {
        val trimmedPrompt = newPrompt.text.trim(' ')
        val updatedFriendsListOnSearch = _friendsUiState.value.friendsList.filter {
            it.firstName.contains(trimmedPrompt, ignoreCase = true) ||
                    it.lastName.contains(trimmedPrompt, ignoreCase = true)
        }

        _friendsUiState.update {
            it.copy(
                searchPrompt = newPrompt,
                friendsListOnSearch = updatedFriendsListOnSearch,
            )
        }
    }

    fun getSortTypeTrailingIcon(sortType: SortType): @Composable (() -> Unit)? {
        if (sortType != _friendsUiState.value.sortType)
            return null

        return {
            when (_friendsUiState.value.sortType) {
                SortType.Default, SortType.Online, SortType.LastSeen ->
                    Icon(Icons.Default.Check, null, Modifier.size(Sizes.iconSmall))

                else -> Icon(
                    imageVector = if (isFriendsListReversed) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(Sizes.iconSmall)
                )
            }
        }
    }

    fun onSortTypeChange(sortType: SortType) {
        val updatedFriendsListSorted: List<Friend>

        if (sortType == _friendsUiState.value.sortType && (sortType == SortType.Default
                    || sortType == SortType.Online || sortType == SortType.LastSeen))
            return

        if (sortType == _friendsUiState.value.sortType) {
            isFriendsListReversed = !isFriendsListReversed
            updatedFriendsListSorted = _friendsUiState.value.friendsListSorted.reversed()
        } else {
            isFriendsListReversed = false
            updatedFriendsListSorted = getSortedFriendsList(_friendsUiState.value.friendsList, sortType)
        }

        _friendsUiState.update {
            it.copy(
                sortType = sortType,
                friendsListSorted = updatedFriendsListSorted,
            )
        }
    }

    private fun getSortedFriendsList(
        friendsList: List<Friend>,
        sortType: SortType = SortType.Default
    ): List<Friend> = when (sortType) {
        SortType.Name -> friendsList.sortedByFirstName()
        SortType.Surname -> friendsList.sortedByLastName()
        SortType.Birthday -> friendsList.sortedByBirthday()
        SortType.LastSeen -> friendsList.sortedByLastSeen()
        SortType.Online -> friendsList.filter(Friend::isOnline)
        else -> friendsList // by importance
    }

}

fun List<Friend>.sortedByFirstName() = sortedWith(compareBy { it.firstName })
fun List<Friend>.sortedByLastName() = sortedWith(compareBy { it.lastName })
fun List<Friend>.sortedByLastSeen() = sortedWith(
    compareByDescending<Friend> { it.isOnline }.thenByDescending { it.lastSeen?.time ?: Int.MIN_VALUE }
)
fun List<Friend>.sortedByBirthday() = sortedWith(
    compareBy { it.birthday?.daysTillBirthday ?: Int.MAX_VALUE }
)
