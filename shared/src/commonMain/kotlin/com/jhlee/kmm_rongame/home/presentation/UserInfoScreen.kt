package com.jhlee.kmm_rongame.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.main.presentation.MainViewModel

@Composable
fun UserInfoScreen(viewModel: MainViewModel) {

    val state = viewModel.state.value
    Column(modifier = Modifier.padding(20.dp)) {
        Row {
            state.userInfo?.let { user ->
                Text(
                    text = getString(
                        SharedRes.strings.user_info_name_title, arrayListOf(user.name)
                    )
                )
            }
        }
        Row {
            Text(text = getString(SharedRes.strings.user_info_money_title))
            state.userInfo?.let { user ->
                Text(text = getString(SharedRes.strings.money_unit, arrayListOf(user.money)))
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }

}