package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    maxRating: Int = 5, starColor: Color = Color.Black, size: Dp = 25.dp
) {
    Row(horizontalArrangement = Arrangement.Center) {
        repeat(maxRating) {
            Icon(

                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = starColor, // 색상 지정
                modifier = Modifier.size(size)
            )
        }
    }
}
