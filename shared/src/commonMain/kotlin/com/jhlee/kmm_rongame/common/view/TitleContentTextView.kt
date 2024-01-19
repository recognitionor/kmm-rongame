package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme

@Composable
fun TitleContentTextView(title: String, contentList: List<String>) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(LightColorScheme.primary, LightColorScheme.onSecondary),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
            .background(brush = gradientBrush, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)).border(
                width = 2.dp, color = LightColorScheme.primary, shape = RoundedCornerShape(8.dp)
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
                .background(LightColorScheme.secondaryContainer).fillMaxWidth()
        )

        Column {
            contentList.forEach {
                // Content
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
            }
        }
    }
}