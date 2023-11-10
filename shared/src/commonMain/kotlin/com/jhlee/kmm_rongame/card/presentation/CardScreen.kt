package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.common.view.PotentialRatingBar
import com.jhlee.kmm_rongame.common.view.StarRatingBar
import com.jhlee.kmm_rongame.constants.GatchaConst
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getPlatformImageResourceBitMap
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
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
    var nameStr = ""
    var nameEngStr = ""
    var grade = 0
    var potential = 0
    var upgrade = 0
    var powerStr = ""
    var textColor = Color.Black
    var cardImg: ImageBitmap? = getPlatformImageResourceBitMap("ic_contact_support")

    if (cardStateValue.gatchaCard != null) {
        cardStateValue.gatchaCard?.let {
            cardImg = getCommonImageResourceBitMap(SharedRes.images.getImageByFileName(it.image))
            nameStr = it.name
            nameEngStr = it.nameEng
            powerStr = it.power.toString()
            grade = (it.grade)
            upgrade = it.upgrade
            potential = it.potential
            color = color
        }
    }

    if (isLoading) {
        val index = (cardStateValue.cardRandomProgress % GradeConst.TYPE_MAP.size)
        color = GradeConst.TYPE_MAP[index]!!.color
        cardImg = getPlatformImageResourceBitMap("ic_contact_support")
        textColor = color
        nameEngStr = ""
        nameStr = ""
        powerStr = ""
        grade = 0
        potential = 0
        upgrade = 0

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
            modifier = Modifier.padding(12.dp)
        ) { // 패딩을 적용할 Box 컴포넌트 추가
            Column {
                Row {
                    StarRatingBar(grade, color, size = 15.dp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = powerStr, modifier = Modifier.weight(0.1f), style = TextStyle(
                            fontSize = 30.sp, fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                cardImg?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (nameStr.isNotBlank()) {
                    Column {
                        Text(
                            text = "$nameStr ($nameEngStr)",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 18.sp, fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            getCommonImageResourceBitMap(SharedRes.images.ic_detail)?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = null,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    PotentialRatingBar(potential, upgrade)
//                    if (potential > 5) {
//                        PotentialRatingBar(
//                            potential - 5, if (upgrade - 5 <= 1) 0 else upgrade - 5
//                        )
//                    }
//                }
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
