package com.jhlee.kmm_rongame.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.getPlatform
import com.jhlee.kmm_rongame.main.presentation.MainViewModel

@Composable
fun SettingScreen(appModule: AppModule, viewModel: MainViewModel, dismiss: () -> Unit) {
    var showMessage by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        backKeyListener = { dismiss.invoke() }
    }

    DisposableEffect(Unit) {
        onDispose { backKeyListener = null }
    }

    Scaffold(topBar = {
        Row {
            getCommonImageResourceBitMap(SharedRes.images.ic_back)?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.clickable { dismiss.invoke() }.width(30.dp).height(30.dp)
                        .padding(5.dp)
                )
            }
            Text(
                text = "설정",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp, fontWeight = FontWeight.Bold
                )
            )
        }
    }) {

        Box {

            Column(modifier = Modifier.padding(20.dp)) {
                Spacer(modifier = Modifier.height(100.dp))
                Button(onClick = {
                    viewModel.initCardInfo(true)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("카드 정보 새로고침")
                }

                Text(text = "버전 정보 : ${getPlatform().version}")
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "추가 되었으면 하는 콘텐츠, quiz 등등은 메일로 남겨주세요 recognitionor@gmail.com")

                Button(onClick = {
                    showMessage = !showMessage
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "제작자의 말")
                }
                if (showMessage) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "회사일을 핑계로 매일 바쁘다고 놀아주지 못하는 나의 아들 에게 이 게임을 바칩니다.\n" +
                                "많은 글자가 나오면 읽기를 거부 하는 나의 아들이 글자를 읽기를 바랍니다.\n" +
                                "행운도 결국 많은 시도로 쟁취할수 있다는 것을 알기를 바랍니다.\n" +
                                "건강하고 자네의 이름처럼 세상에 이롭고 의로운 사람이 되길 바랍니다.\n" +
                                "동탄의 초등 2학년 소년 에게도 응원의 메시지를 보냅니다.\n" +
                                "세상 모든 엄마 아빠 에게 무한한 리스펙을 보냅니다."

                    )
                }
            }
        }

    }
}