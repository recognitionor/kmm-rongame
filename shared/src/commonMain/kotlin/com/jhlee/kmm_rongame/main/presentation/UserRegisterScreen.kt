package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegisterScreen(state: MainState, onClick: (name: String) -> Unit) {
    var userName by remember { mutableStateOf("") } // 사용자 입력값을 저장할 상태 변수
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "이름이 뭐에요?")

            OutlinedTextField(value = userName, placeholder = {
                Text(text = "이름을 입력해주세요.")
            }, onValueChange = {
                userName = it
            }, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()
            )

            // 버튼 추가
            Button(
                onClick = {
                    if (userName.isNotEmpty()) run {
                        onClick.invoke(userName)
                    }
                },

                shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "등록하기")

            }
        }
    }
}
