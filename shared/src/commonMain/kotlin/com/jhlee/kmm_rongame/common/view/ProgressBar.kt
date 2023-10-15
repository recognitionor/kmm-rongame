package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(progress: Float) {
    LinearProgressIndicator(
        progress = progress, // 프로그레스 값 (0.0에서 1.0 사이)
        modifier = Modifier.fillMaxWidth().height(30.dp), // 가로로 전체 폭을 채우도록 설정
        color = Color.Magenta
    )
}