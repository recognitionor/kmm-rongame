package com.jhlee.kmm_rongame.reward.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.bank.presentation.BankScreen
import com.jhlee.kmm_rongame.calendar.presentation.CalendarScreen
import com.jhlee.kmm_rongame.card.presentation.HomeViewModel
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.quiz.presentation.QuizScreen
import com.jhlee.kmm_rongame.quiz.presentation.QuizViewModel
import com.jhlee.kmm_rongame.utils.Utils
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun RewardScreen(mainViewModel: MainViewModel, appModule: AppModule) {

    val rewardViewModel = getViewModel(key = RewardViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { RewardViewModel(appModule.dbAttendDataSource) })
    val quizViewModel = getViewModel(key = HomeViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { QuizViewModel(appModule.dbQuizDataSource) })
    val title = getString(SharedRes.strings.quiz)
    val message = getString(SharedRes.strings.etc_mini_game_quiz)
    val state: RewardState by rewardViewModel.state.collectAsState()

    if (state.rewardScreenSelected == RewardViewModel.REWARD_DEFAULT_SCREEN) {


        mainViewModel.setWholeScreen(false)
        Box(modifier = Modifier.fillMaxSize().padding(50.dp)) {
            Column {
                CalendarScreen(rewardViewModel)
                Button(
                    enabled = !state.attendedList.contains(Utils.getCurrentDateInFormat().toLong()),
                    onClick = {
                        rewardViewModel.attend()
                        mainViewModel.updateUserMoney(RuleConst.ATTEND_REWARD)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = getString(SharedRes.strings.attend))
                }
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    mainViewModel.showDialog(MainState.QUIZ_INFO_DIALOG,
                        createDialog(title, message, "img_smart_dragon", {
                            mainViewModel.dismissDialog()
                            mainViewModel.updateUserMoney(-RuleConst.QUIZ_COST) {
                                if (it) {
                                    rewardViewModel.selectedScreen(RewardViewModel.REWARD_QUIZ_SCREEN)
                                } else {
                                    mainViewModel.showDialog(
                                        MainState.NOT_ENOUGH_MONEY_DIALOG,
                                        createDialog("돈이 모자랍니다.", "", "img_smart_dragon", {
                                            mainViewModel.dismissDialog()
                                        })
                                    )
                                }
                            }
                        }) {
                            mainViewModel.dismissDialog()
                        })
                }) {
                    Text(text = getString(SharedRes.strings.etc_mini_game_quiz))
                }

                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    rewardViewModel.selectedScreen(RewardViewModel.REWARD_BANK_SCREEN)
                }) {
                    Text(text = getString(SharedRes.strings.bank))
                }
            }
        }

    } else {
        Column {

            when (state.rewardScreenSelected) {
                RewardViewModel.REWARD_QUIZ_SCREEN -> {
                    mainViewModel.setWholeScreen(true)
                    QuizScreen(appModule) {
                        mainViewModel.updateUserMoney(it)
                        rewardViewModel.selectedScreen(RewardViewModel.REWARD_DEFAULT_SCREEN)
                    }
                }

                RewardViewModel.REWARD_ETC_SCREEN -> {

                }

                RewardViewModel.REWARD_BANK_SCREEN -> {
                    BankScreen(mainViewModel, appModule) {
                        rewardViewModel.selectedScreen(RewardViewModel.REWARD_DEFAULT_SCREEN)
                    }
                }

                else -> {
                    Button(onClick = {
                        rewardViewModel.selectedScreen(RewardViewModel.REWARD_DEFAULT_SCREEN)
                        quizViewModel.clearQuizInfo()
                    }) {
                        Text(text = "back")
                    }
                }
            }
        }
    }
}
