package com.jhlee.kmm_rongame

import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainScreen
import com.jhlee.kmm_rongame.test.presentation.TestScreen2

@Composable
fun App(appModule: AppModule) {

    MainScreen(appModule)

}