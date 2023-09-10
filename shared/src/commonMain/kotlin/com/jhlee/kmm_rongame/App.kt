package com.jhlee.kmm_rongame

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(appModule: AppModule) {


    MainScreen(appModule)

}