package com.jhlee.kmm_rongame.cardgame.presentaion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.bank.presentation.BankViewModel
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun CardGameMainScreen(mainViewModel: MainViewModel, appModule: AppModule) {
    val viewModel = getViewModel(key = BankViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { CardGameViewModel(appModule.dbCardGameDataSource) })
    val state by viewModel.state.collectAsState()
    Logger.log("state : ${state.enemyEntry}")
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
                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
                ) {
                    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                        items(16) { index ->
                            Box(modifier = Modifier.aspectRatio(1f).padding(4.dp).clickable {
                                viewModel.getCardGame(index)
                            }) {
                                Text(text = index.toString())
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
                        appModule, viewModel, state
                    ) {
                        mainViewModel.setWholeScreen(false)
                        viewModel.setScreen(CardGameViewModel.CARD_GAME_DEFAULT_SCREEN, -1)
                    }
                }
            }
        }
    }
}