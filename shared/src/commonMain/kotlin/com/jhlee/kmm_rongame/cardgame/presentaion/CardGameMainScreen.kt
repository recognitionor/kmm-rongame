package com.jhlee.kmm_rongame.cardgame.presentaion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.bank.presentation.BankViewModel
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainState.Companion.CARD_START_DIALOG
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun CardGameMainScreen(mainViewModel: MainViewModel, appModule: AppModule) {
    val viewModel = getViewModel(key = BankViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { CardGameViewModel(appModule.dbCardGameDataSource) })
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(
            color = Color.White // 기본 배경 색상
        )
    ) {
        getCommonImageResourceBitMap(SharedRes.images.ic_forest)?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(0.4f) // 여기서 0.5는 투명도를 나타냄

            )
        }
        when (state.screenMode) {
            CardGameViewModel.CARD_GAME_DEFAULT_SCREEN -> {
                val currentStage = (mainViewModel.state.value.userInfo?.cardStage ?: 0)
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 50.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                        items(160) { index ->
                            Box(modifier = Modifier.aspectRatio(1f).padding(4.dp).clickable {
                                if (currentStage >= index) {
                                    val costMoney = (index + 1) * 10
                                    mainViewModel.showDialog(
                                        MainState.QUIZ_INFO_DIALOG, createDialog("Start(시작) ",
                                            "$costMoney 원이 듭니다.",
                                            "img_cloud_dragon",
                                            {
                                                mainViewModel.updateUserMoney(-(costMoney))
                                                viewModel.getCardGame(index)
                                                mainViewModel.dismissDialog()
                                            },
                                            {
                                                mainViewModel.dismissDialog()
                                            })
                                    )
                                } else {
                                    mainViewModel.showDialog(
                                        MainState.CARD_NOT_START_DIALOG, createDialog("아직 안되요",
                                            "${(index)}을 통과 하세요",
                                            "img_cloud_dragon",
                                            positiveButtonCallback = {
                                                mainViewModel.dismissDialog()
                                            })
                                    )
                                }


                            }) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = (index + 1).toString(),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    val image = if (currentStage <= index) {
                                        getCommonImageResourceBitMap(SharedRes.images.ic_lock)
                                    } else {
                                        getCommonImageResourceBitMap(SharedRes.images.ic_win)
                                    }
                                    image?.let {
                                        Image(
                                            modifier = Modifier.fillMaxWidth().height(35.dp),
                                            bitmap = it,
                                            contentDescription = null
                                        )
                                    }
                                }

                                // 아이템 내용
                            }
                        }
                    }
                }
            }

            CardGameViewModel.CARD_GAME_ING_SCREEN -> {
                if (state.selectStageIndex >= 0) {
                    mainViewModel.setWholeScreen(true)
                    CardGameScreen(
                        mainViewModel, viewModel, state
                    ) {
                        mainViewModel.setWholeScreen(false)
                        viewModel.setScreen(CardGameViewModel.CARD_GAME_DEFAULT_SCREEN, -1)
                    }
                }
            }
        }
    }
}