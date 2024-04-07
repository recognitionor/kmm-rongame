package com.jhlee.kmm_rongame.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.card.presentation.CardCombinationScreen
import com.jhlee.kmm_rongame.card.presentation.CardDetailInfoScreen
import com.jhlee.kmm_rongame.card.presentation.CardListScreen
import com.jhlee.kmm_rongame.card.presentation.CardScreen
import com.jhlee.kmm_rongame.card.presentation.HomeState
import com.jhlee.kmm_rongame.card.presentation.HomeViewModel
import com.jhlee.kmm_rongame.cardselector.presentaion.CardSelectSearchBar
import com.jhlee.kmm_rongame.common.view.ClickableDefaults
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.pandora.presentation.PandoraListScreen
import com.jhlee.kmm_rongame.setting.SettingScreen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlin.math.sqrt

@Composable

fun HomeScreen(viewModel: MainViewModel, appModule: AppModule) {
    val cardViewModel = getViewModel(key = HomeViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { HomeViewModel(appModule.dbCardDataSource) })
    val cardStateValue by cardViewModel.state.collectAsState()
    val state: MainState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserInfo()
        backKeyListener = null
    }
    when (cardStateValue.homeScreenMode) {
        HomeState.HOME_SCREEN_DEFAULT -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(top = 15.dp, start = 15.dp, end = 15.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight()
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

                    Row {
                        state.userInfo?.let {
                            UserInfoScreen(it)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column(
                            modifier = Modifier.then(ClickableDefaults.getDefaultClickable(
                                pressedAlpha = 0f
                            ) { cardViewModel.selectScreen(HomeState.HOME_SCREEN_SETTING) })
                                .width(80.dp).padding(20.dp)
                        ) {
                            getCommonImageResourceBitMap(SharedRes.images.ic_setting)?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Text(
                                text = "설정",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
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
                                    ).clip(RoundedCornerShape(8.dp))
                                    .then(ClickableDefaults.getDefaultClickable {
                                        cardViewModel.selectScreen(HomeState.HOME_SCREEN_COMBINATION)
                                    })
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

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Card(
                                modifier = Modifier.run {
                                    size(width = (120f * 0.8).dp, height = 120f.dp).padding(10.dp)
                                        .border(
                                            width = 4.dp,
                                            color = Color.Gray,
                                            shape = RoundedCornerShape(8.dp)
                                        ).clip(RoundedCornerShape(8.dp))
                                        .then(ClickableDefaults.getDefaultClickable {
                                            if ((viewModel.state.value.userInfo?.money
                                                    ?: 0) > RuleConst.PANDORA_PRICE
                                            ) {
                                                cardViewModel.selectScreen(HomeState.HOME_SCREEN_PANDORA)
                                            } else {
                                                viewModel.showDialog(
                                                    MainState.INFO_DIALOG, createDialog(
                                                        "돈이 부족해요.",
                                                        "${RuleConst.PANDORA_PRICE}원 이 필요해요",
                                                        null,
                                                        false,
                                                        {
                                                            viewModel.dismissDialog()
                                                        },
                                                        null
                                                    )
                                                )
                                            }
                                        })
                                }, colors = CardDefaults.cardColors(Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) { // 패딩을 적용할 Box 컴포넌트 추가
                                    Spacer(modifier = Modifier.weight(1f))
                                    getCommonImageResourceBitMap(SharedRes.images.ic_open_box)?.let {
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
                                text = "판도라 상자", textAlign = TextAlign.Center
                            )
                        }

                    }
                    CardSelectSearchBar(cardStateValue.searchKeyword,
                        cardStateValue.sortFilter,
                        cardStateValue.isReverseFilter,
                        {
                            cardViewModel.toggleReverseFilter()
                        }) { keyword: String, index: Int ->
                        cardViewModel.searchSortCardList(keyword, index)
                    }
                    CardListScreen(homeViewModel = cardViewModel)
                }
            }
        }

        HomeState.HOME_SCREEN_COMBINATION -> {
            CardCombinationScreen(appModule, viewModel) {
                cardViewModel.getMyCardList()
                cardViewModel.selectScreen(HomeState.HOME_SCREEN_DEFAULT)
            }
        }

        HomeState.HOME_SCREEN_SETTING -> {
            SettingScreen(appModule, viewModel) {
                cardViewModel.selectScreen(HomeState.HOME_SCREEN_DEFAULT)
            }
        }

        HomeState.HOME_SCREEN_PANDORA -> {
            PandoraListScreen(viewModel, appModule) {
                cardViewModel.getMyCardList()
                cardViewModel.selectScreen(HomeState.HOME_SCREEN_DEFAULT)
            }
        }
    }
}