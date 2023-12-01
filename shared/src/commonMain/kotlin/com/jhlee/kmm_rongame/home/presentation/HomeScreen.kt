package com.jhlee.kmm_rongame.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.card.presentation.CardDetailInfoScreen
import com.jhlee.kmm_rongame.card.presentation.CardListScreen
import com.jhlee.kmm_rongame.card.presentation.CardScreen
import com.jhlee.kmm_rongame.card.presentation.CardViewModel
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.ui.AutoSizeImage
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable

fun HomeScreen(viewModel: MainViewModel, appModule: AppModule) {
    val cardViewModel = getViewModel(key = CardViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { CardViewModel(appModule.dbCardDataSource) })
    val cardStateValue by cardViewModel.state.collectAsState()
    val state: MainState by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(15.dp)) {
        Column(
            modifier = Modifier.fillMaxHeight().padding(bottom = 70.dp, start = 16.dp, end = 16.dp)
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
                CardScreen(
                    cardViewModel, viewModel, height = 250f
                )
            }
            CardListScreen(cardViewModel = cardViewModel)
        }
    }
}