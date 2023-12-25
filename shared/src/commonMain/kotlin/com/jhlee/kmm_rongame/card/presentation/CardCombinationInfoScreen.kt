package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.data.CardCombinationInfo
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.core.util.Logger
import kotlin.math.roundToInt

@Composable
fun CardCombinationInfoScreen(
    cardCombinationInfo: List<CardCombinationInfo>,
    buyCombinationInfo: (CardCombinationInfo) -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = 100.dp)) {
        Text(
            text = "<카드 결합 족보>",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray
            )
        )

        LazyVerticalGrid(columns = GridCells.Fixed(1)) {
            items(cardCombinationInfo.size) { index ->
                val cardCombinationInfo = cardCombinationInfo[index]
                val stuffImage =
                    cardCombinationInfo.stuffList.map { rememberBitmapFromBytes(it.image) }
                val cardImg = rememberBitmapFromBytes(cardCombinationInfo.resultCard.image)
                Row(
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(36.dp).clickable {
                        if (!cardCombinationInfo.isOpened) {
                            buyCombinationInfo.invoke(cardCombinationInfo)
                        }
                    }, verticalAlignment = Alignment.CenterVertically
                ) {
                    cardImg?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            colorFilter = if (cardCombinationInfo.isOpened) null else ColorFilter.tint(
                                Color.Black
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Logger.log("${cardCombinationInfo.resultCard.name} - ${cardCombinationInfo.isOpened}")
                        if (cardCombinationInfo.resultCard.grade == 1) {
                            Text(
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                text = "뽑기 카드",
                                style = TextStyle(
                                    fontSize = 16.sp, fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            if (cardCombinationInfo.isOpened) {
                                stuffImage.forEach { imageBitmap ->
                                    imageBitmap?.let {
                                        Image(
                                            bitmap = imageBitmap,
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                            } else {
                                getCommonImageResourceBitMap(SharedRes.images.ic_lock)?.let {
                                    Image(
                                        it, contentDescription = null
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "확률 : ${((cardCombinationInfo.percent / 1000) * 100).roundToInt()}%",
                        style = TextStyle(
                            fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Red
                        )
                    )
                }
            }
        }
    }
}
