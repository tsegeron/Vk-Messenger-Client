package com.laru.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun ProfilePhoto(
    imageModel: Any?,
    imageSize: Dp,
    modifier: Modifier = Modifier,
    @DrawableRes placeholderRes: Int? = null
) {
    val placeholder = placeholderRes?.let { painterResource(id = it) }

    AsyncImage(
        model = imageModel,
        contentDescription = null,
        modifier = modifier
            .size(imageSize)
            .clip(CircleShape),
        placeholder = placeholder,
        fallback = placeholder,
    )
}



