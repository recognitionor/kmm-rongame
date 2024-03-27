package com.jhlee.kmm_rongame.pandora.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes

@Composable
fun PandoraTargetScreen(targetCard: Card) {
    val cardImg = rememberBitmapFromBytes(targetCard.image)
    Column(
        modifier = Modifier.fillMaxWidth().height(150.dp).padding(2.dp) // 두 테두리 사이의 간격
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "목표 카드",
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth()) {
            cardImg?.let {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Image(
                        bitmap = it, contentDescription = null, modifier = Modifier.size(75.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${targetCard.name}(${targetCard.nameEng})", style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                )

                val message = if (targetCard.upgrade == 0) {
                    "카드를 얻으세요"
                } else {
                    "${targetCard.upgrade}번 강화 하세요"
                }
                Text(
                    text = message, style = TextStyle(
                        fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Red
                    )
                )
            }

        }
        Spacer(modifier = Modifier.weight(1f))
    }
}