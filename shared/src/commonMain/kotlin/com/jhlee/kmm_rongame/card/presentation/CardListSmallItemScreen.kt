package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
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
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.constants.CardFilterConst
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme

@Composable
fun CardListSmallItemScreen(
    card: Card? = null,
    modifier: Modifier = Modifier,
    visibleInfoType: Int = CardFilterConst.RECENT,
    height: Float,
    selected: Boolean = false,
    isEnabled: Boolean = true,
    infoMsg: String = "Select!!",
    onItemDetailInfoClick: ((card: Card?) -> Unit)? = null,
    onItemClick: ((card: Card?) -> Unit)? = null,
) {
    val cardWidth = (height * 0.8)

    if (card != null) {
        val color: Color = GradeConst.TYPE_MAP[card.grade - 1]!!.color
        val nameStr = card.name
        val visibleInfo = when (visibleInfoType) {
            CardFilterConst.ID -> {
                "${card.cardId + 1}"
            }

            CardFilterConst.POWER -> {
                card.power
            }

            CardFilterConst.POTENTIAL -> {
                "${card.potential}"
            }

            CardFilterConst.GRADE -> {
                "${card.grade}"
            }

            CardFilterConst.UPGRADE -> {
                "${card.upgrade}"
            }
            CardFilterConst.RECENT -> {
                "${card.power}"
            }

            else -> {
                "${card.cardId + 1}"
            }
        }


        val cardImg = rememberBitmapFromBytes(card.image)
        androidx.compose.material3.Card(colors = CardDefaults.cardColors(
            if (!selected) Color.White else LightColorScheme.primary.copy(
                alpha = 0.3f
            )
        ), modifier = modifier.run {
            size(width = cardWidth.dp, height = height.dp).padding(4.dp)
                .border(width = 2.dp, color = color, shape = RoundedCornerShape(8.dp))
        }.clickable {
            if (onItemClick != null) {
                if (isEnabled) {
                    onItemClick(card)
                }
            }
        }) {
            Box(
                modifier = Modifier.padding(10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row {
                        Text(
                            textAlign = TextAlign.Start,
                            text = nameStr,
                            modifier = Modifier.weight(1f),
                            style = TextStyle(
                                fontSize = 12.sp, fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            textAlign = TextAlign.End,
                            text = visibleInfo.toString(),
                            modifier = Modifier.weight(1f),
                            style = TextStyle(
                                fontSize = 20.sp, fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    cardImg?.let {
                        Image(
                            it,
                            contentDescription = null,
                            alignment = Alignment.TopCenter,
                            modifier = Modifier.fillMaxWidth().size(60.dp)
                        )
                    }


                    Spacer(modifier = Modifier.height(12.dp))
                    getCommonImageResourceBitMap(SharedRes.images.ic_detail)?.let {
                        Image(bitmap = it,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp).clickable {
                                onItemDetailInfoClick?.invoke(card)
                            })
                    }
                }
            }
        }
    } else {
        androidx.compose.material3.Card(colors = CardDefaults.cardColors(Color.White),
            modifier = modifier.run {
                size(width = cardWidth.dp, height = height.dp).padding(4.dp)
                    .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
            }.clickable {
                if (onItemClick != null) {
                    onItemClick(null)
                    if (isEnabled) {

                    }
                }
            }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.weight(1f))
                getCommonImageResourceBitMap(SharedRes.images.ic_choice)?.let {
                    Image(
                        bitmap = it, contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = infoMsg, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
