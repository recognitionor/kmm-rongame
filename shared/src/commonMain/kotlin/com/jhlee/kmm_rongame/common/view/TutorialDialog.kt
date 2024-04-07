package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap

@Composable
fun TutorialDialog(
    useBackBtn: (() -> Unit)?,
    useNextBtn: (() -> Unit)?,
    imageBitmap: ImageBitmap,
    content: String,
    dismiss: () -> Unit
) {

    LaunchedEffect(Unit) {
        backKeyListener = {
            dismiss.invoke()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
        .pointerInput(Unit) {
            detectTapGestures { }
        }) {
        Box(
            modifier = Modifier.padding(20.dp).height(500.dp).fillMaxWidth().background(Color.White)
                .align(Alignment.Center)
        ) {

            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                if (useBackBtn != null) {
                    getCommonImageResourceBitMap(SharedRes.images.ic_back)?.let {
                        Image(bitmap = it,
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier.size(25.dp).clickable { useBackBtn.invoke() })
                    }
                }

                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = null,
                        alignment = Alignment.Center,
                        modifier = Modifier.sizeIn(maxHeight = 200.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = content,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )

                }

                if (useNextBtn != null) {
                    getCommonImageResourceBitMap(SharedRes.images.ic_dialog_next)?.let {
                        Image(bitmap = it,
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier.size(25.dp).clickable { useNextBtn.invoke() })
                    }
                }
            }
            Button(modifier = Modifier.fillMaxWidth().padding(20.dp).align(Alignment.BottomCenter),
                onClick = {
                    dismiss.invoke()
                }) {
                Text(text = "OK")
            }
        }
    }
}