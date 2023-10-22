package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.utils.GameUtils
import dev.icerock.moko.resources.getImageByFileName

@Composable
fun CardDetailInfoScreen(card: Card, onDismiss: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // 수평 방향 가운데 정렬
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            getCommonImageResourceBitMap(SharedRes.images.getImageByFileName(card.image))?.let {
                Image(
                    it, contentDescription = null, modifier = Modifier.width(150.dp)
                )
            }

            Text(
                text = card.name, style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = card.type, style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(20.dp))


            Row {
                Column(
                    modifier = Modifier.padding(bottom = 26.dp)
                ) {
                    Text(
                        text = "${
                            getString(
                                SharedRes.strings.card_detail_att
                            )
                        } ${card.attack}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "${
                            getString(
                                SharedRes.strings.card_detail_def
                            )
                        } ${card.defense}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "${
                            getString(
                                SharedRes.strings.card_detail_spd
                            )
                        } ${card.speed}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "${
                            getString(
                                SharedRes.strings.card_detail_hp
                            )
                        } ${card.hp}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "${
                            getString(
                                SharedRes.strings.card_detail_mp
                            )
                        } ${card.mp}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(36.dp)) // 간격 조정을 위한 Spacer 추가

                Column(
                    modifier = Modifier.padding(bottom = 26.dp)
                ) {
                    Text(
                        text = "${getString(SharedRes.strings.card_gatcha_grade_title)} ${card.grade + 1}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(

                        text = "${getString(SharedRes.strings.card_gatcha_power_title)} ${
                            GameUtils.getPower(
                                card
                            )
                        }", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "${getString(SharedRes.strings.card_gatcha_cost_title)} ${
                            card.cost
                        }", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f)) // 빈 공간을 추가하여 버튼을 아래로 밀어냅니다

            Button(modifier = Modifier.fillMaxWidth(), onClick = { onDismiss.invoke() }) {
                Text("확인")
            }
        }

    }

}