package com.jhlee.kmm_rongame.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.presentation.CardCombinationScreen
import com.jhlee.kmm_rongame.card.presentation.CardDetailInfoScreen
import com.jhlee.kmm_rongame.card.presentation.CardListScreen
import com.jhlee.kmm_rongame.card.presentation.CardScreen
import com.jhlee.kmm_rongame.card.presentation.HomeState
import com.jhlee.kmm_rongame.card.presentation.HomeViewModel
import com.jhlee.kmm_rongame.cardgame.presentaion.CardGameState
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable

fun HomeScreen(viewModel: MainViewModel, appModule: AppModule) {
    val cardViewModel = getViewModel(key = HomeViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { HomeViewModel(appModule.dbCardDataSource) })
    val cardStateValue by cardViewModel.state.collectAsState()
    val state: MainState by viewModel.state.collectAsState()

    when (cardStateValue.homeScreenMode) {
        HomeState.HOME_SCREEN_DEFAULT -> {
            Box(modifier = Modifier.fillMaxSize().padding(15.dp)) {
                Column(
                    modifier = Modifier.fillMaxHeight()
                        .padding(bottom = 70.dp, start = 16.dp, end = 16.dp)
                ) {

                    if (cardStateValue.isShowCardInfoDialog) {
                        cardStateValue.detailCardInfo?.let {
                            CardDetailInfoScreen(it) {
                                cardViewModel.toggleCardInfoDialog(it)
                            }
                        }
                    }

                    if (cardStateValue.updateUserInfo) {
                        viewModel.getUserInfo()
                        cardViewModel.refreshDoneUserInfo()
                    }
                    state.userInfo?.let {
                        UserInfoScreen(it)
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CardScreen(
                                cardViewModel, viewModel, height = 120f
                            )
                            Text(
                                text = "카드 뽑기", textAlign = TextAlign.Center
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Card(modifier = Modifier.run {
                                size(width = (120f * 0.8).dp, height = 120f.dp).padding(10.dp)
                                    .border(
                                        width = 4.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(8.dp)
                                    ).clickable {
                                        cardViewModel.selectScreen(HomeState.HOME_SCREEN_COMBINATION)
                                    }
                            }, colors = CardDefaults.cardColors(Color.White)) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) { // 패딩을 적용할 Box 컴포넌트 추가
                                    Spacer(modifier = Modifier.weight(1f))
                                    getCommonImageResourceBitMap(SharedRes.images.ic_plus)?.let {
                                        Image(
                                            bitmap = it,
                                            contentDescription = null,
                                            alignment = Alignment.Center,
                                            modifier = Modifier.fillMaxWidth().height(35.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            Text(
                                text = "카드 조합", textAlign = TextAlign.Center
                            )
                        }

                    }
                    CardListScreen(cardViewModel = cardViewModel)
                }
            }
        }

        HomeState.HOME_SCREEN_COMBINATION -> {
            CardCombinationScreen(appModule) {
                cardViewModel.getCardList()
                cardViewModel.selectScreen(HomeState.HOME_SCREEN_DEFAULT)
            }
        }
    }


}