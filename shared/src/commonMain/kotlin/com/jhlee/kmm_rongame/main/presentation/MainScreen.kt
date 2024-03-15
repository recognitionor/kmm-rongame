package com.jhlee.kmm_rongame.main.presentation

import PandoraScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.book.presentation.BookScreen
import com.jhlee.kmm_rongame.cardgame.presentaion.CardGameMainScreen
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.home.presentation.HomeScreen
import com.jhlee.kmm_rongame.isAndroid
import com.jhlee.kmm_rongame.reward.presentation.RewardScreen
import com.jhlee.kmm_rongame.test.presentation.TestScreen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun MainScreen(appModule: AppModule) {

    val viewModel = getViewModel(key = MainViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { MainViewModel(appModule.dbMainDataSource) })
    val state: MainState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        backKeyListener = {
            when (state.selectedTab) {
                MainState.NAVIGATION_TAB_HOME -> {
                    backKeyListener = null
                }

                MainState.NAVIGATION_TAB_GAME -> {
                    viewModel.selectedTab(MainState.NAVIGATION_TAB_HOME)
                }

                MainState.NAVIGATION_TAB_BOOK -> {
                    viewModel.selectedTab(MainState.NAVIGATION_TAB_HOME)
                }

                MainState.NAVIGATION_TAB_REWARD -> {
                    viewModel.selectedTab(MainState.NAVIGATION_TAB_HOME)
                }
                MainState.NAVIGATION_TAB_TEST -> {
                    viewModel.selectedTab(MainState.NAVIGATION_TAB_HOME)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { }
    }
    val isTestMode = false
    val navigationBarHeight = if (isAndroid()) 80.dp else 120.dp
//    viewModel.updateUserMoney(100000)
    if (isTestMode) {
        TestScreen(appModule)
    } else {
        Scaffold(bottomBar = {
            if (state.userInfo != null && !state.isWholeScreenOpen) {
                NavigationBar(modifier = Modifier.height(navigationBarHeight)) {
                    MainScreenItem.SCREEN_LIST.forEachIndexed { index, item ->
                        NavigationBarItem(icon = {
                            Icon(
                                item.icon, contentDescription = item.name
                            )
                        },
                            label = { Text(item.name) },
                            selected = state.selectedTab == index,
                            onClick = { viewModel.selectedTab(index) })
                    }
                }
            }
        }) {

            Box(modifier = Modifier.padding(bottom = if (state.isWholeScreenOpen) 0.dp else navigationBarHeight)) {
                if (state.userInfo == null) {
                    UserRegisterScreen(state) {
                        viewModel.registerUser(it)
                    }
                } else {
                    if (state.isCardInfoLoading) {
                        SplashScreen(state)
                    } else {
                        // 여기에서 선택된 아이템에 따라 다른 컴포저블을 표시합니다.
                        when (state.selectedTab) {
                            MainState.NAVIGATION_TAB_HOME -> HomeScreen(viewModel, appModule)
                            MainState.NAVIGATION_TAB_GAME -> CardGameMainScreen(
                                viewModel, appModule
                            )

                            MainState.NAVIGATION_TAB_BOOK -> BookScreen(viewModel, appModule)
                            MainState.NAVIGATION_TAB_REWARD -> RewardScreen(viewModel, appModule)
                            MainState.NAVIGATION_TAB_TEST ->  PandoraScreen(viewModel, appModule)

                        }
                    }
                }
            }
        }
    }


    when (state.openDialog) {
        MainState.NO_DIALOG -> {}
        else -> {
            state.dialog?.invoke()
        }
    }
}


