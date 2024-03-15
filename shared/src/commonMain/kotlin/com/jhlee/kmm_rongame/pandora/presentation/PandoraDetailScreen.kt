package com.jhlee.kmm_rongame.pandora.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.jhlee.kmm_rongame.ui.theme.Blue300
import com.jhlee.kmm_rongame.ui.theme.Blue500
import com.jhlee.kmm_rongame.ui.theme.Green300
import com.jhlee.kmm_rongame.ui.theme.Green500
import com.jhlee.kmm_rongame.ui.theme.Red300
import com.jhlee.kmm_rongame.ui.theme.Red500
import com.jhlee.kmm_rongame.ui.theme.Yellow300
import com.jhlee.kmm_rongame.ui.theme.Yellow500

@Composable
fun PandoraDetailScreen(card: Card) {

    Box(modifier = Modifier.padding(50.dp).fillMaxSize()) {
        Column {
            Row {
                PandoraDetailItem(
                    "이름 : ", card.name, modifier = Modifier.background(
                        Brush.linearGradient(
                            listOf(
                                Red300, Red500
                            )
                        ), shape = RoundedCornerShape(10.dp)
                    ).weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                PandoraDetailItem(
                    "등급 : ", card.grade.toString(), modifier = Modifier.background(
                        Brush.linearGradient(
                            listOf(
                                Yellow300, Yellow500
                            )
                        ), shape = RoundedCornerShape(10.dp)
                    ).weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                PandoraDetailItem(
                    "최대 강화 수", card.upgrade.toString(), modifier = Modifier.background(
                        Brush.linearGradient(
                            listOf(
                                Blue300, Blue500
                            )
                        ), shape = RoundedCornerShape(10.dp)
                    ).weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                PandoraDetailItem(
                    "현재 강화 수", card.upgrade.toString(), modifier = Modifier.background(
                        Brush.linearGradient(
                            listOf(
                                Green300, Green500
                            )
                        ), shape = RoundedCornerShape(10.dp)
                    ).weight(1f)
                )
            }
        }

    }
}

@Composable
fun PandoraDetailItem(title: String, content: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.height(50.dp).clip(RoundedCornerShape(10.dp)).padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxHeight(), text = title, style = TextStyle(
                    fontSize = 15.sp, fontWeight = FontWeight.Light, color = Color.White
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxHeight(), text = content, style = TextStyle(
                    fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            )

        }
    }
}