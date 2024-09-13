package com.laru.ui.composable

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun Button(
    modifier: Modifier = Modifier // implement default?
) {
    FilledTonalButton({}) {
        Text(text = "FilledButton")
    }
}
