package com.laru.ui.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    contentModifier: Modifier = Modifier, // for the row with leading/trailing icon, placeholder, value
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onCancel: () -> Unit = { onValueChange(TextFieldValue()) },
    enabled: Boolean = true,
    isError: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholderText: String? = null, // so its width could be measured
    trailingIcon: @Composable (() -> Unit)? = defaultTrailingIcon(
        value.text, onCancel, enabled, isError, interactionSource.collectIsFocusedAsState().value
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        autoCorrectEnabled = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = MaterialTheme.shapes.large, // medium
    colors: TextFieldColors = TextFieldDefaults.basicInputTextFieldColors(),
) {
    val isFocused = interactionSource.collectIsFocusedAsState().value

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = textStyle,
            interactionSource = interactionSource,
            decorationBox = decorationBox(
                pickedColors = colorPicker(enabled, isError, isFocused, colors),
                placeholderText = placeholderText,
                textStyle = textStyle,
                shape = shape,
                focused = isFocused,
                isValueEmpty = value.text.isEmpty(),
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                rowModifier = contentModifier
            )
        )
    }
}

private fun decorationBox(
    pickedColors: ColorPicker,
    placeholderText: String?,
    textStyle: TextStyle,
    shape: Shape,
    focused: Boolean,
    isValueEmpty: Boolean,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    rowModifier: Modifier = Modifier
): @Composable (innerTextField: @Composable () -> Unit) -> Unit = { innerTextField ->
    var measuredTextWidth by remember { mutableStateOf(0.dp) }
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    if (placeholderText != null) {
        LaunchedEffect(placeholderText) {
            val textLayoutResult = textMeasurer.measure(placeholderText, style = textStyle, maxLines = 1)
            measuredTextWidth = with(density) { textLayoutResult.size.width.toDp() }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxHeight()
            .background(pickedColors.containerColor, shape)
            .then(
                if (focused) { // TODO: provide proper color
                    Modifier.border(1.dp, shape = shape, color = pickedColors.trailingIconColor)
                } else Modifier
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        val startPadding by animateDpAsState(
            targetValue = if (focused || !isValueEmpty) {
                PaddingValues(Paddings.small).calculateStartPadding(LayoutDirection.Ltr)
            } else (maxWidth - (Sizes.iconDefault + Paddings.small + measuredTextWidth)) / 2,
            label = "StartPaddingDpAnimation"
        )

        Row(
            modifier = Modifier
                .padding(start = startPadding, end = Paddings.small)
                .then(rowModifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides pickedColors.leadingIconColor) {
                leadingIcon?.invoke()
            }
            Spacer(Modifier.width(Spacers.small))
            if (isValueEmpty && placeholderText != null) {
                CompositionLocalProvider(LocalContentColor provides pickedColors.placeholderColor) {
                    Text(text = placeholderText)
                }
            } else {
                CompositionLocalProvider(LocalContentColor provides pickedColors.innerTextFieldColor) {
                    Box(Modifier.weight(1f)) {
                        innerTextField()
                    }
                }
                CompositionLocalProvider(LocalContentColor provides pickedColors.trailingIconColor) {
                    Box(Modifier.size(Sizes.iconDefault)) {
                        trailingIcon?.invoke()
                    }
                }
            }
        }
    }
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
                modifier = Modifier.size(Sizes.iconDefault)
            )
        }
    }
    value.isNotEmpty() && enabled -> {
        {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(id = R.string.clear),
                modifier = Modifier
                    .size(Sizes.iconDefault)
                    .clickable { onClick() }
            )
        }
    }
    else -> null
}

// Change default colors
@Composable
fun TextFieldDefaults.basicInputTextFieldColors(
    focusedTextColor: Color = Color.Unspecified,
    unfocusedTextColor: Color = Color.Unspecified,
    disabledTextColor: Color = Color.Unspecified,
    errorTextColor: Color = Color.Unspecified,
    focusedContainerColor: Color = Color.Unspecified,
    unfocusedContainerColor: Color = Color.Unspecified,
    disabledContainerColor: Color = Color.Unspecified,
    errorContainerColor: Color = Color.Unspecified,
    focusedLeadingIconColor: Color = Color.Unspecified,
    unfocusedLeadingIconColor: Color = Color.Unspecified,
    disabledLeadingIconColor: Color = Color.Unspecified,
    errorLeadingIconColor: Color = Color.Unspecified,
    focusedPlaceholderColor: Color = Color.Unspecified,
    unfocusedPlaceholderColor: Color = Color.Unspecified,
    disabledPlaceholderColor: Color = Color.Unspecified,
    errorPlaceholderColor: Color = Color.Unspecified,
    focusedTrailingIconColor: Color = Color.Unspecified,
    unfocusedTrailingIconColor: Color = Color.Unspecified,
    disabledTrailingIconColor: Color = Color.Unspecified,
    errorTrailingIconColor: Color = Color.Unspecified,
    textSelectionColors: TextSelectionColors? = null
//        TextSelectionColors(
//        handleColor = Color.Magenta,
//        backgroundColor = Color.Yellow
//    ),
): TextFieldColors = colors(
    focusedTextColor = focusedTextColor,
    unfocusedTextColor = unfocusedTextColor,
    disabledTextColor = disabledTextColor,
    errorTextColor = errorTextColor,
    focusedContainerColor = focusedContainerColor,
    unfocusedContainerColor = unfocusedContainerColor,
    disabledContainerColor = disabledContainerColor,
    errorContainerColor = errorContainerColor,
    focusedLeadingIconColor = focusedLeadingIconColor,
    unfocusedLeadingIconColor = unfocusedLeadingIconColor,
    disabledLeadingIconColor = disabledLeadingIconColor,
    errorLeadingIconColor = errorLeadingIconColor,
    focusedPlaceholderColor = focusedPlaceholderColor,
    unfocusedPlaceholderColor = unfocusedPlaceholderColor,
    disabledPlaceholderColor = disabledPlaceholderColor,
    errorPlaceholderColor = errorPlaceholderColor,
    focusedTrailingIconColor = focusedTrailingIconColor,
    unfocusedTrailingIconColor = unfocusedTrailingIconColor,
    disabledTrailingIconColor = disabledTrailingIconColor,
    errorTrailingIconColor = errorTrailingIconColor,
    selectionColors = textSelectionColors,
)

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
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)

                Row(modifier) {
                    TextButton(
                        onClick = {},
                        border = BorderStroke(2.dp, Color.White),
                        modifier = Modifier.height(40.dp).width(80.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    BasicInputTextField(
                        modifier = Modifier.weight(1f),
                        value = TextFieldValue("Enabled"),
//                        focused = false,
                        onValueChange = {},
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    )

                    Spacer(Modifier.width(12.dp))

                    TextButton(
                        onClick = {},
                        border = BorderStroke(2.dp, Color.White),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                        )
                    }
                }
                BasicInputTextField(
                    modifier = modifier,
                    value = TextFieldValue(),
                    onValueChange = {},
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    placeholderText = "Search"
                )

                BasicInputTextField(
                    modifier = modifier,
                    value = TextFieldValue("Error"),
                    onValueChange = {},
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    isError = true
                )
                BasicInputTextField(
                    modifier = modifier,
                    value = TextFieldValue(),
                    onValueChange = {},
                    placeholderText = "Phone or Email",
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    enabled = false
                )
            }
        }
    }
}
