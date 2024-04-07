package com.jhlee.kmm_rongame.collector.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.cardselector.presentaion.CardSelectDialog
import com.jhlee.kmm_rongame.common.view.ClickableDefaults
import com.jhlee.kmm_rongame.common.view.StoryDialog
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlin.math.min

@Composable
fun CardCollectorScreen(mainViewModel: MainViewModel, appModule: AppModule, dismiss: () -> Unit) {
    val viewModel = getViewModel(key = CardCollectorViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { CardCollectorViewModel(appModule.dbCollectorDataSource) })

    val mainState by mainViewModel.state.collectAsState()
    val state by viewModel.state.collectAsState()

    val commentList = remember { mutableStateOf<List<String>>(emptyList()) }
    if (commentList.value.isEmpty()) {
        commentList.value = arrayListOf(
            getString(SharedRes.strings.collector_comment_1),
            getString(SharedRes.strings.collector_comment_2),
            getString(SharedRes.strings.collector_comment_3),
            getString(SharedRes.strings.collector_comment_4),
            getString(SharedRes.strings.collector_comment_5),
            getString(SharedRes.strings.collector_comment_6),
            getString(SharedRes.strings.collector_comment_7),
            getString(SharedRes.strings.collector_comment_8),
            getString(SharedRes.strings.collector_comment_9),
        )
    }


    LaunchedEffect(Unit) {
        mainViewModel.setWholeScreen(true)
        backKeyListener = {
            if (state.screenMode == CardCollectorState.CARD_SELECT_SCREEN) {
                viewModel.selectScreen(CardCollectorState.DEFAULT_SCREEN, null)
            } else {
                dismiss.invoke()
            }
        }
        mainViewModel.setWholeScreen(true)
    }

    DisposableEffect(Unit) {
        onDispose {
            mainViewModel.setWholeScreen(true)
            mainViewModel.dismissDialog()
            mainViewModel.setWholeScreen(false)
        }
    }

    Scaffold(topBar = {
        Row {
            getCommonImageResourceBitMap(SharedRes.images.ic_back)?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.clickable { dismiss.invoke() }.width(30.dp).height(30.dp)
                        .padding(5.dp)
                )
            }
        }
    }) {

        Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            getCommonImageResourceBitMap(SharedRes.images.ic_collector_bg)?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().alpha(0.1f).align(Alignment.BottomCenter)
                )
            }
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(50.dp))
                Row(modifier = ClickableDefaults.getDefaultClickable(pressedAlpha = 0.0f) {
                    viewModel.addCommentIndex()
                }) {
                    getCommonImageResourceBitMap(SharedRes.images.ic_collector)?.let {
                        Image(
                            bitmap = it, contentDescription = null, modifier = Modifier.size(150.dp)
                        )
                    }
                    val comment = commentList.value[state.commentIndex % commentList.value.size]
                    StoryDialog(content = comment)
                }

                Button(
                    onClick = {
                        viewModel.getCardSelectList()
                        viewModel.selectScreen(CardCollectorState.CARD_WASTE_SELECT_SCREEN)
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "카드1장에 10원으로 팔기")
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Adjust the value as needed for your desired spacing
                ) {
                    items(state.cardCollectorWantedItemList.size) {
                        val cardCollectorWantedItem = state.cardCollectorWantedItemList[it]
                        val cardImg = rememberBitmapFromBytes(cardCollectorWantedItem.card?.image)
                        Column {
                            Row(
                                modifier = Modifier.height(200.dp).border(
                                    width = 3.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ).padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (cardImg != null) {
                                    when (it % 4) {
                                        0 -> {
                                            Column(
                                                Modifier.weight(1f).padding(8.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Image(
                                                    bitmap = cardImg,
                                                    contentDescription = null,
                                                    alignment = Alignment.Center
                                                )
                                                Text(
                                                    text = "이 카드를 갖고 있니?",
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        1 -> {
                                            Text(
                                                text = "영어로 ${cardCollectorWantedItem.card?.nameEng} 카드를 갖고 있다면 나에게 팔아줄래?",
                                                Modifier.weight(1f),
                                                style = TextStyle(
                                                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }

                                        2 -> {
                                            Column(
                                                Modifier.weight(1f).padding(8.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Image(
                                                    bitmap = cardImg,
                                                    contentDescription = null,
                                                    colorFilter = ColorFilter.tint(Color.Black),
                                                    alignment = Alignment.Center
                                                )
                                                Text(
                                                    text = "이렇게 생긴 카드 인데...",
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        3 -> {
                                            Text(
                                                text = "${
                                                    cardCollectorWantedItem.card?.description?.substring(
                                                        0, min(
                                                            25,
                                                            cardCollectorWantedItem.card?.description?.length ?: 25
                                                        )
                                                    )
                                                }...\n이 설명의 카드 뭘까?",
                                                Modifier.weight(1f),
                                                style = TextStyle(
                                                    fontSize = 16.sp, fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "별 ${cardCollectorWantedItem.grade} 개 카드를 모으고 있어",
                                        Modifier.weight(1f),
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f).padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = "필요한 갯수 : ${cardCollectorWantedItem.count}",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "업그레이드 횟수 : ${cardCollectorWantedItem.upgrade}",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "필요 파워 : ${cardCollectorWantedItem.power}",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "보상금 : ${cardCollectorWantedItem.reward}",
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            viewModel.getCardSelectList(cardCollectorWantedItem)
                                            viewModel.selectScreen(
                                                CardCollectorState.CARD_SELECT_SCREEN,
                                                cardCollectorWantedItem
                                            )
                                        }, modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = "카드 팔기")
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
    if (state.selectedCardCollectorWantedItem != null) {
        if (state.selectList.isEmpty()) {
            if (!state.isLoading) {
                mainViewModel.showDialog(
                    MainState.INFO_DIALOG,
                    createDialog = createDialog("", "팔수 있는 카드가 없어", "ic_collector", false, {
                        mainViewModel.dismissDialog()
                        viewModel.selectScreen(CardCollectorState.DEFAULT_SCREEN, null)
                    })
                )
            }
        } else {
            CardCollectorSelectDialog(viewModel, state.selectedCardCollectorWantedItem!!) {
                viewModel.selectScreen(CardCollectorState.DEFAULT_SCREEN, null)
            }
        }
    }

    if (state.screenMode == CardCollectorState.CARD_WASTE_SELECT_SCREEN && state.selectList.isNotEmpty()) {
        CardSelectDialog(state.selectList, 20, isForceMaxSelect = false, {
            viewModel.wasteCard(it)
        }, {
            viewModel.selectScreen(CardCollectorState.DEFAULT_SCREEN)
        })
    }
}