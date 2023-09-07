package com.jhlee.kmm_rongame

import androidx.compose.ui.window.ComposeUIViewController
import com.jhlee.kmm_rongame.di.AppModule
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController {
    App(AppModule())
}