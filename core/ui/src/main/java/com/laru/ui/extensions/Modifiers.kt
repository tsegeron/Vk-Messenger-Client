package com.laru.ui.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed


//
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    this
}
