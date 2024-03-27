package com.jhlee.kmm_rongame.pandora.presentation

import PandoraScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.ktor.util.Identity.decode

@Composable
fun PandoraListScreen(mainViewModel: MainViewModel, appModule: AppModule) {
    val viewModel =
        getViewModel(key = PandoraListViewModel.VIEWMODEL_KEY, factory = viewModelFactory {
            PandoraListViewModel(
                appModule.dbPandoraDataSource
            )
        })


    val state: PandoraListState by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        backKeyListener = {
            mainViewModel.selectedTab(MainState.NAVIGATION_TAB_HOME)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            backKeyListener = null
        }
    }


    when (state.pandoraScreen) {
        PandoraListState.SCREEN_GAME -> {
            PandoraScreen(mainViewModel, appModule) {
                viewModel.selectScreen(PandoraListState.SCREEN_LIST)
                viewModel.getStageList()
                mainViewModel.getUserInfo()
            }
        }

        PandoraListState.SCREEN_LIST -> {

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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.padding(top = 20.dp, start = 14.dp, end = 14.dp)
                ) {
                    items(state.stageList.size) { index ->
                        Column {
                            PandoraListItemView(
                                state.stageList[index],
                                index,
                                mainViewModel.state.value.userInfo?.cardStage ?: 0
                            ) {
                                viewModel.selectScreen(PandoraListState.SCREEN_GAME)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }

    }
}