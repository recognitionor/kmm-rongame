package com.jhlee.kmm_rongame.book.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.common.view.StarRatingBar
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme

@Composable
fun BookContentTextView(book: Card, contentList: List<Pair<String, String>>) {
    val color = GradeConst.TYPE_MAP[book.grade]?.color ?: Color.Black
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(LightColorScheme.secondaryContainer, color.copy(alpha = 0.4f)),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
            .background(brush = gradientBrush, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)).border(
                width = 2.dp, color = color.copy(alpha = 0.4f), shape = RoundedCornerShape(8.dp)
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.background(Color.White).fillMaxWidth().height(25.dp).padding(5.dp)
        ) {
            StarRatingBar(
                book.grade, starColor = color, 15.dp
            )
        }


        Column(modifier = Modifier.fillMaxWidth().padding(start = 6.dp)) {
            contentList.forEach {
                // Content
                Row {
                    Text(
                        text = it.first, style = TextStyle(
                            fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black
                        )
                    )
                    Text(
                        text = it.second, style = TextStyle(
                            fontSize = 15.sp, fontWeight = FontWeight.Thin, color = Color.Black
                        )
                    )
                }

            }
        }
    }
}