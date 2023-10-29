package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import kotlinx.coroutines.delay

@Composable
fun StoryDialog(modifier: Modifier = Modifier.fillMaxWidth(), content: String) {
    var isBlinking by remember { mutableStateOf(true) }
    val alpha = if (isBlinking) 1f else 0f


    LaunchedEffect(isBlinking) {
        while (true) {
            delay(800L)
            isBlinking = !isBlinking

        }
    }


    Box(
        modifier = modifier.padding(16.dp)
    ) {
        val borderSize = 2.dp
        val borderColor = Color.Black
        val cornerRadius = 16.dp

        Column(
            modifier = Modifier.fillMaxWidth().border(
                width = borderSize, color = borderColor, shape = RoundedCornerShape(cornerRadius)
            )
        ) {
            // 대사 내용을 추가
            Text(
                text = content, modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                getCommonImageResourceBitMap(SharedRes.images.ic_dialog_next)?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                        modifier = Modifier.alpha(alpha).size(20.dp, 20.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}