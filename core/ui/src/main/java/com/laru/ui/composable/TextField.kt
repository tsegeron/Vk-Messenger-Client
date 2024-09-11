package com.laru.ui.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.common.preview.ThemePreviews
import com.laru.ui.R
import com.laru.ui.model.Paddings
import com.laru.ui.model.Sizes
import com.laru.ui.model.Spacers


@Composable
fun BasicInputTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = { onValueChange(TextFieldValue()) },
    enabled: Boolean = true,
    isError: Boolean = false,
    focused: MutableState<Boolean> = remember { mutableStateOf(false) },
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = defaultTrailingIcon(
        value.text, onCancel, enabled, isError, focused.value
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        autoCorrectEnabled = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.large,
    colors: TextFieldColors = TextFieldDefaults.colors( // TODO: provide other default colors
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    ),
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(Sizes.textField)
            .focusRequester(focusRequester)
            .onFocusChanged { focused.value = it.isFocused },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            val pickedColors = colorPicker(enabled, isError, focused.value, colors)

            Box(Modifier.fillMaxSize().background(pickedColors.containerColor, shape))

            BoxWithConstraints(contentAlignment = Alignment.CenterStart) {
                // maxWidth –> maxWidth of BasicTextField
                // 84.dp –> leadingIcon + padding + Text("Search")
                // (maxWidth - 84.dp)/2  -> middle of the container
                // for now I don't know how to find ideal center of the container based on
                // [BasicTextField, leadingIcon, padding, text] widths without using
                // `Modifier.onSizeChanged` or bunch of `BoxWithConstraints`
                val startPadding by animateDpAsState(
                    targetValue = if (focused.value) {
                        PaddingValues(Paddings.small).calculateStartPadding(LayoutDirection.Ltr)
                    } else (maxWidth - 84.dp)/2,
                    label = "StartPaddingDpAnimation"
                )

                Row(
                    modifier = Modifier
                        .padding(start = startPadding, end = Paddings.small)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalContentColor provides pickedColors.leadingIconColor) {
                        leadingIcon?.invoke()
                    }
                    Spacer(Modifier.width(Spacers.small))
                    if (value.text.isEmpty()) {
                        CompositionLocalProvider(LocalContentColor provides pickedColors.placeholderColor) {
                            placeholder?.invoke()
                        }
                    } else {
                        CompositionLocalProvider(LocalContentColor provides pickedColors.innerTextFieldColor) {
                            Box(Modifier.weight(1f)) { innerTextField() }
                        }
                        CompositionLocalProvider(LocalContentColor provides pickedColors.trailingIconColor) {
                            // TODO: box trailingIcon into 20.dp size (set maxSize = 20.dp)
                            trailingIcon?.invoke()
                        }
                    }
                }
            }
        }
    )
}

private fun defaultTrailingIcon(
    value: String,
    onClick: () -> Unit,
    enabled: Boolean,
    isError: Boolean,
    isFocused: Boolean,
): @Composable (() -> Unit)? = when {
    isError && !isFocused -> {
        {
            Icon(
                painter = painterResource(R.drawable.ic_error_24),
                contentDescription = stringResource(id = R.string.error),
                modifier = Modifier.size(Sizes.icon)
            )
        }
    }
    value.isNotEmpty() && enabled -> {
        {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(id = R.string.clear),
                modifier = Modifier
                    .size(Sizes.icon)
                    .clickable { onClick() }
            )
        }
    }
    else -> null
}

@Stable
private fun colorPicker(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
    colors: TextFieldColors,
): ColorPicker {
    val containerColor: Color
    val leadingIconColor: Color
    val placeholderColor: Color
    val innerTextFieldColor: Color
    val trailingIconColor: Color

    when {
        !enabled -> {
            containerColor = colors.disabledContainerColor
            leadingIconColor = colors.disabledLeadingIconColor
            placeholderColor = colors.disabledPlaceholderColor
            innerTextFieldColor = colors.disabledTextColor
            trailingIconColor = colors.disabledTrailingIconColor
        }
        isError -> {
            containerColor = colors.errorContainerColor
            leadingIconColor = colors.errorLeadingIconColor
            placeholderColor = colors.errorPlaceholderColor
            innerTextFieldColor = colors.errorTextColor
            trailingIconColor = colors.errorTrailingIconColor
        }
        focused -> {
            containerColor = colors.focusedContainerColor
            leadingIconColor = colors.focusedLeadingIconColor
            placeholderColor = colors.focusedPlaceholderColor
            innerTextFieldColor = colors.focusedTextColor
            trailingIconColor = colors.focusedTrailingIconColor
        }
        else -> {
            containerColor = colors.unfocusedContainerColor
            leadingIconColor = colors.unfocusedLeadingIconColor
            placeholderColor = colors.unfocusedPlaceholderColor
            innerTextFieldColor = colors.unfocusedTextColor
            trailingIconColor = colors.unfocusedTrailingIconColor
        }
    }

    return ColorPicker(
        containerColor = containerColor,
        leadingIconColor = leadingIconColor,
        placeholderColor = placeholderColor,
        innerTextFieldColor = innerTextFieldColor,
        trailingIconColor = trailingIconColor,
    )
}

private data class ColorPicker(
    val containerColor: Color,
    val leadingIconColor: Color,
    val placeholderColor: Color,
    val innerTextFieldColor: Color,
    val trailingIconColor: Color,
)


@ThemePreviews
@Composable
private fun BasicInputTextFieldPreview() {
    VKMessengerTheme {
        // Since there is an animation the composable stays in its initial state
        Surface {
            val isFocused = remember {
                mutableStateOf(true)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BasicInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = TextFieldValue("Enabled"),
                    focused = isFocused,
                    onValueChange = {},
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                )
                BasicInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = TextFieldValue(),
                    onValueChange = {},
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text(text = "Search") }
                )

                BasicInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = TextFieldValue("Error"),
                    onValueChange = {},
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    isError = true
                )
                BasicInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = TextFieldValue(),
                    onValueChange = {},
                    placeholder = { Text(text = "Search") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    enabled = false
                )
            }
        }
    }
}
