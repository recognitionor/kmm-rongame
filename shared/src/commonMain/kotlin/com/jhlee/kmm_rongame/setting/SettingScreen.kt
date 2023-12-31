package com.jhlee.kmm_rongame.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.jhlee.kmm_rongame.Platform
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.getPlatform
import com.jhlee.kmm_rongame.main.presentation.MainViewModel

@Composable
fun SettingScreen(appModule: AppModule, viewModel: MainViewModel, dismiss: () -> Unit) {
    var showMessage by remember { mutableStateOf(false) }
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
                        text = "게임을 좋아하하는 현시점 6살의 나의 아들에게 도파민의 홍수속에서 도파민의 노예가 되지말고 보다 잘 자라기를 바라는 마음에서 만들어 보았습니다. " + "아비는 게임회사에 다니지만 게임을 만드는 사람은 아니다 보니 많은 부침과 더딤이 있었습니다. " + "많이 부족 하지만 주경야독 하며 부지런히 만들어 여기 까지 오게 되었습니다." + "이 세상 모든 엄마 아빠 들 에게 존경심을 표하며 이 앱을 올려봅니다."
                    )
                }
            }
        }

    }
}