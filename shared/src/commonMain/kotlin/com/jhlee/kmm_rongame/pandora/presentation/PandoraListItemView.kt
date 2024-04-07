package com.jhlee.kmm_rongame.pandora.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.core.util.Logger


@Composable
fun PandoraListItemView(card: Card, currentIndex: Int, myStage: Int, onClick: () -> Unit) {
    val cardImg = rememberBitmapFromBytes(card.image)
    val cardStageIndex = myStage / 10
    val stageUpgradeIndex = myStage % 10
    val isLockedStage by remember { mutableStateOf(currentIndex > cardStageIndex) }

    Card(
        modifier = Modifier.height(200.dp).border(
            width = 4.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)
        )
    ) {
        if (isLockedStage) {
            Box(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                getCommonImageResourceBitMap(SharedRes.images.ic_lock)?.let { img ->
                    Image(
                        bitmap = img, contentDescription = null, modifier = Modifier.size(70.dp)
                    )
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                if (currentIndex >= cardStageIndex) {
                    onClick.invoke()
                }
            }) {
                Box(modifier = Modifier.weight(1f).padding(8.dp)) {
                    cardImg?.let {
                        Image(
                            bitmap = it,
                            colorFilter = if (stageUpgradeIndex > 0) null else ColorFilter.tint(
                                Color.Black
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.Center) {
                    if (currentIndex < cardStageIndex) {
                        getCommonImageResourceBitMap(SharedRes.images.ic_win)?.let { it1 ->
                            Image(
                                alignment = Alignment.Center,
                                bitmap = it1,
                                contentDescription = null,
                                modifier = Modifier.size(70.dp).fillMaxWidth()
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp)
                        ) {
                            items(10) { index ->
                                val image = if (stageUpgradeIndex - 1 > index) {
                                    SharedRes.images.ic_potential_on
                                } else {
                                    SharedRes.images.ic_potential_off
                                }
                                getCommonImageResourceBitMap(image)?.let { it1 ->
                                    Image(
                                        bitmap = it1,
                                        contentDescription = null,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}