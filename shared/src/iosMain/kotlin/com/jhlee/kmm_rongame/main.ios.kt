package com.jhlee.kmm_rongame

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.ui.helper.LocalSafeArea
import platform.UIKit.UIViewController

@Suppress("unused", "FunctionName")
fun MainViewController(
    topSafeArea: Float,
    bottomSafeArea: Float
): UIViewController {

    return ComposeUIViewController {
        val density = LocalDensity.current

        val topSafeAreaDp = with(density) { topSafeArea.toDp() }
        val bottomSafeAreaDp = with(density) { bottomSafeArea.toDp() }
        val safeArea = PaddingValues(top = topSafeAreaDp + 10.dp, bottom = bottomSafeAreaDp)

        // Bind safe area as the value for LocalSafeArea
        CompositionLocalProvider(LocalSafeArea provides safeArea) {
            Surface(modifier = Modifier.padding(
                top = safeArea.calculateTopPadding()
            )) {
                App(AppModule())
            }
        }
    }
}
