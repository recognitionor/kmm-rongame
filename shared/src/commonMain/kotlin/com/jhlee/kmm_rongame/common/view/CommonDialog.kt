package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


fun createDialog(
    title: String = "",
    message: String = "",
    positiveButtonCallback: () -> Unit,
    nativeButtonCallback: (() -> Unit)? = null
): @Composable () -> Unit = {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
            .pointerInput(Unit) {
                detectTapGestures { }
            }, contentAlignment = Alignment.Center
    ) {

        Card(colors = CardDefaults.cardColors(Color.White), modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title, style = TextStyle(
                        fontSize = 28.sp, fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(text = message)

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {

                    Button(onClick = {
                        positiveButtonCallback.invoke()
                    }) {
                        Text(text = "확인")
                    }

                    nativeButtonCallback?.let {
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(onClick = {
                            nativeButtonCallback.invoke()
                        }) {
                            Text(text = "취소")
                        }
                    }
                }
            }
        }
    }
}