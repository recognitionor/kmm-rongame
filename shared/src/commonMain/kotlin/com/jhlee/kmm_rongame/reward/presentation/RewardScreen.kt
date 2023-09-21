package com.jhlee.kmm_rongame.reward.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.quiz.presentation.QuizScreen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun RewardScreen(viewModel: MainViewModel, appModule: AppModule) {

    val viewModel = getViewModel(
        key = MainViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { RewardViewModel() })
    val state: RewardState by viewModel.state.collectAsState()

    if (state.rewardScreenSelected == RewardViewModel.REWARD_DEFAULT_SCREEN) {
        Box(modifier = Modifier.fillMaxSize().padding(50.dp)) {
            Column {
                Button(onClick = {
                    viewModel.selectedScreen(RewardViewModel.REWARD_QUIZ_SCREEN)
                }) {
                    Text(text = "quiz")
                }

                Button(onClick = {}) {
                    Text(text = "reward")
                }

            }
        }
    } else {
        Column {
            Button(onClick = {
                viewModel.selectedScreen(RewardViewModel.REWARD_DEFAULT_SCREEN)
            }) {
                Text(text = "back")
            }

            when (state.rewardScreenSelected) {
                RewardViewModel.REWARD_QUIZ_SCREEN -> {
                    QuizScreen(appModule)
                }
                RewardViewModel.REWARD_ETC_SCREEN -> {

                }

            }
        }

    }
}