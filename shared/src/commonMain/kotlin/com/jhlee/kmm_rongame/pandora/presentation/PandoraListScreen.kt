package com.jhlee.kmm_rongame.pandora.presentation

import PandoraScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.ktor.util.Identity.decode

@Composable
fun PandoraListScreen(mainViewModel: MainViewModel, appModule: AppModule, dismiss: () -> Unit) {
    val viewModel =
        getViewModel(key = PandoraListViewModel.VIEWMODEL_KEY, factory = viewModelFactory {
            PandoraListViewModel(
                appModule.dbPandoraDataSource
            )
        })


    val state: PandoraListState by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        backKeyListener = {
            dismiss.invoke()
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
            backKeyListener = {
                dismiss.invoke()
            }

            Scaffold(topBar = {
                Row {
                    getCommonImageResourceBitMap(SharedRes.images.ic_back)?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier.clickable(true) {
                                dismiss.invoke()
                            }.width(30.dp).height(30.dp).padding(5.dp)
                        )
                    }

                    Text(
                        text = "판도라의 상자",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 24.sp, fontWeight = FontWeight.Bold
                        )
                    )
                }
            }) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 60.dp).background(
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
                                    mainViewModel.state.value.userInfo?.pandoraStage ?: 0
                                ) {
                                    mainViewModel.showDialog(
                                        MainState.INFO_DIALOG,
                                        createDialog(
                                            "${RuleConst.PANDORA_PRICE}원",
                                            "${RuleConst.PANDORA_PRICE}원이 차감 됩니다.",
                                            null,
                                            useBackKey = true,
                                            {
                                                mainViewModel.dismissDialog()
                                                mainViewModel.updateUserMoney(-RuleConst.PANDORA_PRICE)
                                                viewModel.selectScreen(PandoraListState.SCREEN_GAME)
                                            },
                                            {
                                                mainViewModel.dismissDialog()
                                            })
                                    )

                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
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