package com.jhlee.kmm_rongame

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainScreen
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme

var backKeyListener: (() -> Unit?)? = null

@Composable
fun App(appModule: AppModule) {
    MaterialTheme(colorScheme = LightColorScheme) {
        MainScreen(appModule)
    }
}