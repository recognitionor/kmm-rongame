package com.jhlee.kmm_rongame.test.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun TestScreen2(appModule: AppModule) {
    val viewModel = getViewModel(key = MainViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { TestViewModel2(appModule.dbTestDataSource) })

    val state = viewModel.state.collectAsState().value

    Column {
        Button(onClick = {
            viewModel.test()
        }) {
            Text(text = "${state.count}")
        }

        TestScreen3(state.count)


    }

}