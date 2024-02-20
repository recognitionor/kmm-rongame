package com.jhlee.kmm_rongame

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme


@Composable
fun App() {
    MaterialTheme(colorScheme = LightColorScheme) {
        MainScreen()
    }
}