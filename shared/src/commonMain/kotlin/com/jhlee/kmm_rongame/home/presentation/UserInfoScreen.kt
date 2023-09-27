package com.jhlee.kmm_rongame.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.domain.UserInfo

@Composable
fun UserInfoScreen(userInfo: UserInfo) {
    Column(modifier = Modifier.padding(20.dp)) {
        Row {
            Text(
                text = getString(
                    SharedRes.strings.user_info_name_title, arrayListOf(userInfo.name)
                )
            )
        }
        Row {
            Text(text = getString(SharedRes.strings.user_info_money_title))
            Text(text = getString(SharedRes.strings.money_unit, arrayListOf(userInfo.money)))
        }
    }

}