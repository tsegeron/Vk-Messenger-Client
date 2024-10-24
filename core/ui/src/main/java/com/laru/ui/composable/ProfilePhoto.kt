package com.laru.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.laru.ui.R


@Composable
fun ProfilePhoto(
    imageModel: Any?,
    imageSize: Dp,
    modifier: Modifier = Modifier,
    @DrawableRes placeholderRes: Int? = null
) {
    val placeholder = placeholderRes?.let { painterResource(id = it) } // TODO remake placeholder
//    val placeholder = painterResource(id = R.drawable.ic_error_white_24)

    if (imageModel == null || (imageModel is String && imageModel.isEmpty())) {
        Box(Modifier.size(40.dp).background(Color.LightGray, shape = CircleShape))
    } else {
        // TODO provide ProfilePic based on VK.NO_PHOTO url
        /* cases:
            1. VK.NO_PHOTO -> provide Box with Text with initials
            2. AsyncImage failed -> provide Box with Text with initials
            3. AsyncImage succeeded -> initial photo
         */
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
}



