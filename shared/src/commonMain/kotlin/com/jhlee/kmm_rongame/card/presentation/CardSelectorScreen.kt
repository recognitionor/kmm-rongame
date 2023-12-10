package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CardSelectorScreen(modifier: Modifier) {
    Card(modifier = modifier.run {
        padding(10.dp).border(
            width = 4.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)
        ).background(Color.White).clickable {}
    }) {
    }
}