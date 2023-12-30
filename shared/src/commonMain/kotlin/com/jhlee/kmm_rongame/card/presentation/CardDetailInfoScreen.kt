package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.data.CardUtils
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.common.view.PotentialRatingBar
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes

@Composable
fun CardDetailInfoScreen(card: Card, onDismiss: () -> Unit) {
    val color: Color = GradeConst.TYPE_MAP[card.grade - 1]!!.color

    val cardImg = rememberBitmapFromBytes(card.image)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 방향 가운데 정렬
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth().border(
                    width = 2.dp, color = color, shape = RoundedCornerShape(12.dp)
                ).padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center, // Row 내에서 가로 방향 가운데 정렬
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    cardImg?.let {
                        Image(
                            bitmap = cardImg,
                            contentDescription = null,
                            modifier = Modifier.weight(1f, true).padding(24.dp),
                            contentScale = ContentScale.Crop
                        )
                    }


                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${card.name}(${card.nameEng})", style = TextStyle(
                                fontSize = 18.sp, fontWeight = FontWeight.Bold
                            )
                        )

                        Text(
                            text = "${getString(SharedRes.strings.card_gatcha_power_title)}${card.power}",
                            style = TextStyle(
                                fontSize = 18.sp, fontWeight = FontWeight.Bold
                            )
                        )

                        if (CardUtils.isUpgradeCard(card)) {
                            Text(
                                text = "${getString(SharedRes.strings.card_gatcha_grade_title)}${card.grade}",
                                style = TextStyle(
                                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                                )
                            )

                            Text(
                                text = "${getString(SharedRes.strings.card_detail_potential)}${card.potential}",
                                style = TextStyle(
                                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                                )
                            )

                            Text(
                                text = "${getString(SharedRes.strings.card_detail_upgrade)}${card.upgrade}",
                                style = TextStyle(
                                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier.fillMaxWidth().border(
                    width = 2.dp, color = color, shape = RoundedCornerShape(12.dp)
                ).padding(20.dp)
            ) {
                Text(
                    text = "${card.description}", style = TextStyle(
                        fontSize = 14.sp, fontWeight = FontWeight.Light
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            if (CardUtils.isUpgradeCard(card)) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    PotentialRatingBar(card.potential, card.upgrade, 25.dp)
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().border(
                        width = 2.dp, color = Color.Red, shape = RoundedCornerShape(12.dp)
                    ).padding(20.dp)
                ) {
                    Text(
                        text = "이 카드는 속성 카드로 다른 카드를 업그레이드 하는 용도 로만 사용이 가능 합니다.", style = TextStyle(
                            fontSize = 14.sp, fontWeight = FontWeight.Light
                        )
                    )
                }
            }

            Row {
                Column(
                    modifier = Modifier.padding(bottom = 26.dp)
                ) {

                }
                Spacer(modifier = Modifier.width(36.dp)) // 간격 조정을 위한 Spacer 추가
            }

            Column {
                val iterator = card.type.iterator()
                val sb = StringBuilder()
                var count = 0

                Row {
                    Column(modifier = Modifier.weight(1f).height(150.dp)) {
                        Text(
                            color = Color.DarkGray,
                            text = "${card.name} 카드가 강한 카드들",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().border(
                                width = 2.dp, color = Color.Green, shape = RoundedCornerShape(12.dp)
                            ).padding(12.dp)
                        ) {
                            cardTypeToMap(card.type, true).forEach { type ->
                                Text(text = "${type.key} + ${type.value * 10}%")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f).height(150.dp)) {
                        Text(
                            color = Color.DarkGray,
                            text = "${card.name} 카드가 약한 카드들",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().border(
                                width = 2.dp, color = Color.Red, shape = RoundedCornerShape(12.dp)
                            ).padding(12.dp)
                        ) {
                            cardTypeToMap(card.type, false).forEach { type ->
                                Text(text = "${type.key} + ${type.value * 10}% 피해")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // 빈 공간을 추가하여 버튼을 아래로 밀어냅니다

            Button(modifier = Modifier.fillMaxWidth(), onClick = { onDismiss.invoke() }) {
                Text("확인")
            }
            Spacer(modifier = Modifier.weight(1f)) // 빈 공간을 추가하여 버튼을 아래로 밀어냅니다
        }
    }
}

fun cardTypeToMap(type: HashSet<CardType>, isString: Boolean): HashMap<String, Int> {
    val tempSet = hashMapOf<String, Int>()
    if (isString) {
        type.forEach {
            it.strongList.forEach { type ->
                if (tempSet.contains(type.key)) {
                    tempSet[type.key] = type.value + type.value
                } else {
                    tempSet[type.key] = type.value
                }
            }
        }
    } else {
        type.forEach {
            it.weakList.forEach { type ->
                if (tempSet.contains(type.key)) {
                    tempSet[type.key] = type.value + type.value
                } else {
                    tempSet[type.key] = type.value
                }
            }
        }
    }

    return tempSet
}