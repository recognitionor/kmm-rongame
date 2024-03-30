package com.jhlee.kmm_rongame.pandora.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.Progress
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.common.view.ProgressBar
import com.jhlee.kmm_rongame.ui.theme.Blue300
import com.jhlee.kmm_rongame.ui.theme.Blue500
import com.jhlee.kmm_rongame.ui.theme.Green300
import com.jhlee.kmm_rongame.ui.theme.Green500
import com.jhlee.kmm_rongame.ui.theme.LighterGray
import com.jhlee.kmm_rongame.ui.theme.Orange
import com.jhlee.kmm_rongame.ui.theme.QuizBorder
import com.jhlee.kmm_rongame.ui.theme.Red300
import com.jhlee.kmm_rongame.ui.theme.Red500
import com.jhlee.kmm_rongame.ui.theme.Yellow300
import com.jhlee.kmm_rongame.ui.theme.Yellow500

@Composable
fun PandoraDetailScreen(card: Card) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp)
                    .background(LighterGray, shape = RoundedCornerShape(24.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${card.name}(${card.nameEng})",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "이름", style = TextStyle(fontSize = 12.sp)
                    )
                }

                Box(
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp).fillMaxHeight()
                        .width(2.dp).background(Color.LightGray)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val type = StringBuilder()
                    card.type.forEachIndexed { index, cardType ->
                        if (card.type.size - 1 == index) {
                            type.append(cardType.name)
                        } else {
                            type.append(cardType.name + ", ")
                        }
                    }

                    Text(
                        text = "$type",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "타입", style = TextStyle(fontSize = 12.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Column {
                Spacer(modifier = Modifier.height(4.dp))
                PandoraDetailInfoItem("등급", card.grade, 10, Red300, Red500)
                Spacer(modifier = Modifier.height(4.dp))
                PandoraDetailInfoItem("잠재력", card.potential, 10, Yellow300, Yellow500)
                Spacer(modifier = Modifier.height(4.dp))
                PandoraDetailInfoItem("강화", card.upgrade, card.potential, Green300, Green500)

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PandoraDetailItem(
                            "파워", modifier = Modifier.background(
                                Brush.linearGradient(
                                    listOf(
                                        Blue300, Blue500
                                    )
                                ), shape = RoundedCornerShape(10.dp)
                            ).width(100.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp).height(10.dp))

                        Text(
                            text = card.power.toString(),
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }

                    Box(
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp).fillMaxHeight()
                            .width(2.dp).background(Color.LightGray)
                    )

                    Row(
                        modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PandoraDetailItem(
                            "획득", modifier = Modifier.background(
                                Brush.linearGradient(
                                    listOf(
                                        Blue300, Blue500
                                    )
                                ), shape = RoundedCornerShape(10.dp)
                            ).width(100.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = card.count.toString(),
                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PandoraDetailInfoItem(
    title: String, value: Int, maxValue: Int, startColor: Color, endColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        PandoraDetailItem(
            title, modifier = Modifier.background(
                Brush.linearGradient(
                    listOf(
                        startColor, endColor
                    )
                ), shape = RoundedCornerShape(10.dp)
            ).width(100.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = value.toString(),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.width(6.dp))

        LinearProgressIndicator(
            strokeCap = StrokeCap.Round,
            progress = (value.toFloat() / maxValue.toFloat()),
            modifier = Modifier.height(15.dp)
                .border(2.dp, Color.Transparent, RoundedCornerShape(15.dp)),
            color = Orange
        )


    }
}

@Composable
fun PandoraDetailItem(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.height(40.dp).clip(RoundedCornerShape(10.dp)).padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxHeight(), text = title, style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            )
        }
    }
}