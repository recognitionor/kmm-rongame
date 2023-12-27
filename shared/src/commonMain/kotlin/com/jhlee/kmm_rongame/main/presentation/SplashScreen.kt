package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Yellow)
    ) {
        Column(modifier = Modifier.padding(50.dp)) {
            Text(text = "안녕안녕")
        }
    }
}