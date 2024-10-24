package com.laru.ui.extensions

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue


fun TextFieldValue.trim(vararg trimmedChars : Char) : TextFieldValue {
    val textToTrim = text
    val trimmedText = StringBuilder()
    var selectionStart = selection.start
    var selectionEnd = selection.end

    textToTrim.forEachIndexed { index, c ->
        if (c in trimmedChars) {
            val shiftedIndex = index + 1
            if (selection.start <= shiftedIndex) { selectionStart-- }
            if (selection.end <= shiftedIndex) { selectionEnd-- }
        } else {
            trimmedText.append(c)
        }
    }
    return copy(text = trimmedText.toString(), selection = TextRange(selectionStart, selectionEnd))
}
