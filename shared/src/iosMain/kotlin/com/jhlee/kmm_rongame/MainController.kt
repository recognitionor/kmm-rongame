package com.jhlee.kmm_rongame

import androidx.compose.ui.window.ComposeUIViewController
import com.jhlee.kmm_rongame.di.AppModule

fun MainController() = ComposeUIViewController {
    App(AppModule())
}