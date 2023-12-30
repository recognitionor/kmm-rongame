package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jhlee.kmm_rongame.cardgame.presentaion.CardGameMainScreen
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.home.presentation.HomeScreen
import com.jhlee.kmm_rongame.reward.presentation.RewardScreen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun MainScreen(appModule: AppModule) {

    val selectedItem = remember { mutableStateOf(0) }

    val viewModel = getViewModel(key = MainViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { MainViewModel(appModule.dbMainDataSource) })
    val state: MainState by viewModel.state.collectAsState()
    if (state.isLoading) {
        SplashScreen()
    } else {
        Scaffold(bottomBar = {
            if (state.userInfo != null && !state.isWholeScreenOpen) {
                NavigationBar {
                    MainScreenItem.SCREEN_LIST.forEachIndexed { index, item ->
                        NavigationBarItem(icon = {
                            Icon(
                                item.icon, contentDescription = item.name
                            )
                        },
                            label = { Text(item.name) },
                            selected = selectedItem.value == index,
                            onClick = { selectedItem.value = index })
                    }
                }
            }
        }) {
            Box {
                if (state.userInfo == null) {
                    UserRegisterScreen(state) {
                        viewModel.registerUser(it)
                    }
                } else {
                    // 여기에서 선택된 아이템에 따라 다른 컴포저블을 표시합니다.
                    when (selectedItem.value) {
                        0 -> HomeScreen(viewModel, appModule)
                        1 -> CardGameMainScreen(viewModel, appModule)
                        2 -> RewardScreen(viewModel, appModule)
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


