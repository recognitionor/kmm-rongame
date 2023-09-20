package com.jhlee.kmm_rongame.test.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.core.util.Logger

@Composable
fun TestScreen3(viewModel: Int) {
    Logger.log("TestScreen3")
    Text(text = viewModel.toString())
}