package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.home.presentation.HomeScreen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(appModule: AppModule) {

    val selectedItem = remember { mutableStateOf(0) }

    val viewModel = getViewModel(key = MainViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { MainViewModel(appModule.dbMainDataSource) })
    val state: MainState by viewModel.state
    Logger.log("${state.userInfo == null}")


    Scaffold(bottomBar = {
        if (state.userInfo != null) {
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
        if (state.userInfo == null) {
            UserRegisterScreen(state) {
                viewModel.registerUser(it)
            }
        } else {
            // 여기에서 선택된 아이템에 따라 다른 컴포저블을 표시합니다.
            when (selectedItem.value) {
                0 -> HomeScreen(viewModel, appModule)
                1 -> Text(text = "1")
                2 -> Text(text = "2")
            }
        }


    }
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}