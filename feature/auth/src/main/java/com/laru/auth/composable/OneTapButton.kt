package com.laru.auth.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import com.laru.app.ui.theme.VKMessengerTheme
import com.laru.auth.R
import com.laru.common.preview.ThemePreviews


@Composable
fun OneTapButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    shape: Shape = MaterialTheme.shapes.extraLarge,
    content: @Composable RowScope.() -> Unit = {
        Text(text = stringResource(R.string.sign_in), style = MaterialTheme.typography.bodyLarge)
    },
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        shape = shape,
        content = content
    )
}


@ThemePreviews
@Composable
private fun OneTapButtonPreview() {
    VKMessengerTheme {
        OneTapButton({})
    }
}
