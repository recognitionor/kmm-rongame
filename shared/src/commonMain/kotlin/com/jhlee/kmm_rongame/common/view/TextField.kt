@file:OptIn(ExperimentalMaterial3Api::class)

package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getString

@Composable
fun CustomStyledTextField(
    value: String, onValueChange: (String) -> Unit
) {
    CustomTextField(value = value,
        onValueChange = onValueChange,
        label = getString(SharedRes.strings.search_book),
        leadingIcon = {
            Icon(
                Icons.Default.Search, contentDescription = "search icon"
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var isFocused by remember { mutableStateOf(false) }

    val textColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    val borderColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
    }

    OutlinedTextField(value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(label, color = textColor) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = true,
        textStyle = TextStyle(color = textColor),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            // 수정된 부분
            cursorColor = textColor,
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            // Handle Done action if needed
        }),
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth().height(56.dp).onFocusChanged {
            isFocused = it.isFocused
        })
}
