package com.jhlee.kmm_rongame.test.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jhlee.kmm_rongame.di.AppModule
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun TestScreen(appModule: AppModule) {
    val viewModel = getViewModel(key = TestViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { TestViewModel(appModule) })
    val state by viewModel.state.collectAsState()

    Text(text = state.result)
}