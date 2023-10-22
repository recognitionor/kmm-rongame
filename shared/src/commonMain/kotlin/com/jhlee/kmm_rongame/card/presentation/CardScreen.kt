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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.common.view.StarRatingBar
import com.jhlee.kmm_rongame.constants.GatchaConst
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getPlatformImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.utils.GameUtils
import dev.icerock.moko.resources.getImageByFileName

@Composable
fun CardScreen(
    cardViewModel: CardViewModel, mainViewModel: MainViewModel, height: Float
) {
    val cardWidth = (height * 0.8)
    val cardStateValue by cardViewModel.state.collectAsState()
    val mainStateValue by mainViewModel.state.collectAsState()
    val isLoading = cardStateValue.isLoading
    var color: Color = GradeConst.TYPE_MAP[0]!!.color
    var powerStr = "?"
    var costStr = "?"
    var nameStr = "???"
    var gradeStr = "?"
    var textColor = Color.Black
    var cardImg: ImageBitmap? = getPlatformImageResourceBitMap("ic_contact_support")

    if (cardStateValue.gatchaCard != null) {
        if (!cardStateValue.isLoadDone) {

        }
        powerStr = GameUtils.getPower(cardStateValue.gatchaCard!!).toString()
        costStr = cardStateValue.gatchaCard!!.cost.toString()
        cardImg =
            getCommonImageResourceBitMap(SharedRes.images.getImageByFileName(cardStateValue.gatchaCard!!.image))
        nameStr = cardStateValue.gatchaCard!!.name
        gradeStr = (cardStateValue.gatchaCard!!.grade + 1).toString()
        color = GradeConst.TYPE_MAP[cardStateValue.gatchaCard!!.grade]!!.color
        textColor = Color.Black
//        cardViewModel.setFlagCardStateLoadDone()
    }

    if (isLoading) {
        val index = (cardStateValue.cardRandomProgress % GradeConst.TYPE_MAP.size)
        color = GradeConst.TYPE_MAP[index]!!.color
        cardImg = getPlatformImageResourceBitMap("ic_contact_support")
        textColor = color
    }

    Card(modifier = Modifier.run {
        size(width = cardWidth.dp, height = height.dp).padding(10.dp)
            .border(width = 4.dp, color = color, shape = RoundedCornerShape(8.dp)).clickable {
                if ((mainStateValue.userInfo?.money ?: 0) >= GatchaConst.GATCHA_COST) {
                    cardViewModel.gatchaCard()
                }
            }
    }, colors = CardDefaults.cardColors(Color.White)) {
        Box(
            modifier = Modifier.padding(18.dp)
        ) { // 패딩을 적용할 Box 컴포넌트 추가
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${getString(SharedRes.strings.card_gatcha_grade_title)} $gradeStr",
                                textAlign = TextAlign.Right,
                                fontSize = 14.sp,
                                color = textColor
                            )
                            Text(
                                text = "${getString(SharedRes.strings.card_gatcha_power_title)} $powerStr",
                                textAlign = TextAlign.Right,
                                fontSize = 14.sp,
                                color = textColor
                            )
                            Text(
                                text = "${getString(SharedRes.strings.card_gatcha_cost_title)} $costStr",
                                textAlign = TextAlign.Right,
                                fontSize = 14.sp,
                                color = textColor
                            )
                        }

                        getPlatformImageResourceBitMap("ic_info")?.let {
                            Image(bitmap = it, contentDescription = "", modifier = Modifier.size(
                                (cardWidth * 0.135).dp, (height * 0.135).dp
                            ).clickable(cardStateValue.gatchaCard != null) {
                                cardViewModel.toggleCardInfoDialog(cardStateValue.gatchaCard)
                            })
                        }
                    }
                }
                cardImg?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "",
                        modifier = Modifier.size(
                            (cardWidth * 0.4).dp, (height * 0.4).dp
                        ).background(Color.White),
                    )
                }
                Text(text = nameStr, textAlign = TextAlign.Center)
                StarRatingBar(((cardStateValue.gatchaCard?.grade?.plus(1)) ?: 0), color)
            }
        }
    }
    // 다이얼로그 표시
//    if (showInfoDialog) {
//        cardStateValue.card?.let {
//            CardDetailDialog(
//                it
//            ) { cardViewModel.onInfoDialogDismissed() }
//        } // 다이얼로그 닫기 함수 호출
//    }

}
