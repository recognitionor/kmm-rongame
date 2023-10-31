package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumberInputField(
    maxValue: Int, onValueChange: (Int) -> Unit, modifier: Modifier = Modifier
) {
    val minValue = 100
    var textValue by remember { mutableStateOf(100.toString()) }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }

    BasicTextField(
        value = textValue, onValueChange = {
            var tempValue = it
            try {
                if (it.isEmpty()) {
                    tempValue = minValue.toString()
                }
                textValue = if (tempValue.toInt() > maxValue) {
                    maxValue.toString()
                } else {
                    tempValue
                }
                val newValue = textValue.toIntOrNull() ?: minValue
                onValueChange(newValue)
            } catch (e: Exception) {
                onValueChange(0)
            }
        }, keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ), singleLine = true, modifier = modifier.then(Modifier.focusRequester(focusRequester))
    )
}