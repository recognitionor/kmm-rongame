package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.jhlee.kmm_rongame.common.view.StarRatingBar
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.seiko.imageloader.rememberAsyncImagePainter
import dev.icerock.moko.resources.getImageByFileName

@Composable
fun CardListItemScreen(
    card: Card,
    visibleInfoType: Int = -1,
    height: Float,
    selected: Boolean = false,
    isEnabled: Boolean = true,
    onItemDetailInfoClick: ((card: Card?) -> Unit)? = null,
    onItemClick: ((card: Card) -> Unit)? = null,
) {
    val cardWidth = (height * 0.8)
    val cardImg = rememberBitmapFromBytes(card.image)
    val color: Color = GradeConst.TYPE_MAP[card.grade - 1]!!.color
    val nameStr = card.name
    val nameEngStr = card.nameEng
    val grade = card.grade
    var potential = card.potential
    var upgrade = card.upgrade
    val powerStr = card.power.toString()
    var textColor = color
    androidx.compose.material3.Card(colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier.run {
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
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            Column {
                Row {
                    Box(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                        StarRatingBar(grade, color, size = 12.dp)
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Text(
                        text = powerStr, modifier = Modifier.weight(1f), style = TextStyle(
                            fontSize = 20.sp, fontWeight = FontWeight.Bold
                        ), textAlign = TextAlign.End
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                cardImg?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier.fillMaxWidth().size(60.dp)
                    )
                }

                if (nameStr.isNotBlank()) {
                    Column {
                        Text(
                            text = "$nameStr ($nameEngStr)",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 14.sp, fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            getCommonImageResourceBitMap(SharedRes.images.ic_detail)?.let {
                                Image(bitmap = it,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp).clickable {
                                        onItemDetailInfoClick?.invoke(card)
                                    })
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
