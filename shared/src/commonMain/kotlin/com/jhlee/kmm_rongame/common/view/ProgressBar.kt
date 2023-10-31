package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.ui.theme.QuizBorder

@Composable
fun ProgressBar(progress: Float) {
    LinearProgressIndicator(
        strokeCap = StrokeCap.Round,
        progress = progress,
        modifier = Modifier.fillMaxWidth().height(30.dp)
            .border(2.dp, QuizBorder, RoundedCornerShape(15.dp)),
        color = Color.Magenta
    )
}