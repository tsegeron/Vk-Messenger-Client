package com.laru.friends.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.input.TextFieldValue
import com.laru.data.model.Friend
import com.laru.friends.R


/**
 * Friends Screen Ui state
 *
 * @property friendsList initial values came from the server (sorted by importance)
 * @property searchPrompt search TextField value
 * @property friendsListOnSearch friendsList filtered by searchPrompt
 * @property sortType how friendsList should be filtered/sorted
 * @property friendsListSorted friendsList filtered/sorted by sortType
 */
data class FriendsUiState(
    val friendsList: List<Friend> = emptyList(),
//    val closestBirthdays: List<Friend> = emptyList(),

    val searchPrompt: TextFieldValue = TextFieldValue(),
    val friendsListOnSearch: List<Friend> = emptyList(),

    val sortType: SortType = SortType.LastSeen,
    val friendsListSorted: List<Friend> = emptyList(),
) {
    /**
     * Defines how friendsList should be filtered/sorted
     *
     * @param sortTypeResId [StringRes] value
     *
     * @property Default sorted by importance; no reverse
     * @property Online filtered by online; no reverse
     * @property LastSeen sorted by online then last seen; no reverse
     * @property Name sorted by name; can be reversed
     * @property Surname sorted by surname; can be reversed
     * @property Birthday sorted by birthday; can be reversed
     */
    enum class SortType(@StringRes val sortTypeResId: Int) {
        Default(R.string.sort_importance),     // 1. online; 2. lastSeen, no reverse
        Online(R.string.sort_online),       // only online, no reverse
        LastSeen(R.string.sort_last_seen),  // , no reverse
        Name(R.string.sort_name),
        Surname(R.string.sort_surname),
        Birthday(R.string.sort_birthday),   // closest birthday first
    }
}
