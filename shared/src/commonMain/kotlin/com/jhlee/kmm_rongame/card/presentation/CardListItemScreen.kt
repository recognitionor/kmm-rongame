package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.constants.GameConst
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.utils.GameUtils

@Composable
fun CardListItemScreen(
    card: Card,
    visibleInfoType: Int = -1,
    height: Float,
    selected: Boolean = false,
    isEnabled: Boolean = true,
    onItemClick: ((card: Card) -> Unit)? = null
) {
    val showInfoDialog = remember { mutableStateOf(false) }
    val cardWidth = (height * 0.8)
    val cardImg: String = card.image

    val powerStr: String = GameUtils.getPower(card).toString()
    val costStr: String = card.cost.toString()

    val nameStr: String = card.name
    val gradeStr: String = (card.grade + 1).toString()
    val color: Color = GradeConst.TYPE_MAP[card.grade]!!.color
    val textColor: Color = Color.Black

    androidx.compose.material3.Card(modifier = Modifier
        .run {
            size(width = cardWidth.dp, height = height.dp)
                .padding(4.dp)
                .border(width = 2.dp, color = color, shape = RoundedCornerShape(8.dp))
        }
        .clickable {
            if (onItemClick == null) {
                showInfoDialog.value = true
            } else {
                if (isEnabled) {
                    onItemClick(card)
                }

            }
        }) {
        Box(
            modifier = Modifier.padding(5.dp)
        ) {


            // 패딩을 적용할 Box 컴포넌트 추가
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    if (selected && isEnabled) {
//                        Image(
//                            modifier = Modifier
//                                .size(100.dp)
//                                .align(Alignment.TopEnd),
//                            getImageResourceBitMap(SharedRes.images.),
//                            contentDescription = "Selected",
//                            colorFilter = ColorFilter.tint(color)
//                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${getString(SharedRes.strings.hello_world)} $gradeStr",
                                textAlign = TextAlign.Right,
                                fontSize = 8.sp,
                                color = textColor
                            )
                            Text(
                                text = "${getString(SharedRes.strings.card_gatcha_power_title)} $powerStr",
                                textAlign = TextAlign.Right,
                                fontSize = 8.sp,
                                color = textColor
                            )
                            Text(
                                text = "${getString(SharedRes.strings.card_gatcha_cost_title)} $costStr",
                                textAlign = TextAlign.Right,
                                fontSize = 8.sp,
                                color = textColor
                            )

                            when (visibleInfoType) {

                                GameConst.GAME_SELECTED_CARD_TYPE_ATT -> {
                                    Text(
                                        text = getString(SharedRes.strings.card_detail_att, listOf(card.attack)),
                                        textAlign = TextAlign.Right,
                                        fontSize = 8.sp,
                                        color = textColor
                                    )
                                }

                                GameConst.GAME_SELECTED_CARD_TYPE_DEF -> {
                                    Text(
                                        text = getString(SharedRes.strings.card_detail_def, listOf(card.defense)),
                                        textAlign = TextAlign.Right,
                                        fontSize = 8.sp,
                                        color = textColor
                                    )
                                }

                                GameConst.GAME_SELECTED_CARD_TYPE_SPD -> {
                                    Text(
                                        text = getString(SharedRes.strings.card_detail_spd, listOf(card.speed)),
                                        textAlign = TextAlign.Right,
                                        fontSize = 8.sp,
                                        color = textColor
                                    )
                                }

                                GameConst.GAME_SELECTED_CARD_TYPE_HP -> {
                                    Text(
                                        text = getString(SharedRes.strings.card_detail_hp, listOf(card.hp)),
                                        textAlign = TextAlign.Right,
                                        fontSize = 8.sp,
                                        color = textColor
                                    )
                                }

                                GameConst.GAME_SELECTED_CARD_TYPE_MP -> {
                                    Text(
                                        text = getString(SharedRes.strings.card_detail_mp, listOf(card.mp)),
                                        textAlign = TextAlign.Right,
                                        fontSize = 8.sp,
                                        color = textColor
                                    )
                                }
                            }
                        }

                        getImageResourceBitMap(SharedRes.images.ic_info)?.let {
                            Image(
                                bitmap = it,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(
                                        (cardWidth * 0.135).dp, (height * 0.135).dp
                                    )
                                    .clickable {
                                        showInfoDialog.value = true
                                    },
                                colorFilter = ColorFilter.tint(color)
                            )
                        }
                    }
                }
                if (isEnabled) {
//                    Image(
//                        imagePath = cardImg,
//                        contentDescription = "",
//                        modifier = Modifier
//                            .size(
//                                (cardWidth * 0.4).dp, (height * 0.4).dp
//                            )
//                            .background(Color.White)
//                    )
                } else {
                    getImageResourceBitMap(SharedRes.images.ic_close)?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "",
                            modifier = Modifier
                                .size(
                                    (cardWidth * 0.4).dp, (height * 0.4).dp
                                )
                                .background(Color.White),
                            colorFilter = ColorFilter.tint(color)
                        )
                    }
                }

                Text(text = nameStr, textAlign = TextAlign.Center)
//                StarRatingBar(((card.grade.plus(1)) ?: 0), color, 12.dp)
            }
        }
    }
    // 다이얼로그 표시
//    if (showInfoDialog.value) {
//        CardDetailDialog(card) {
//            showInfoDialog.value = false
//        }
//    }

}
