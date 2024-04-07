package com.jhlee.kmm_rongame.cardselector.presentaion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.presentation.CardDetailInfoScreen
import com.jhlee.kmm_rongame.card.presentation.CardListSmallItemScreen
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun CardSelectDialog(
    // selectMax 값이 0 이하면 하나만 선택 하는 다이얼로그
    list: List<Card>,
    selectMax: Int = -1,
    isForceMaxSelect: Boolean = true,
    selectListener: (List<Card>) -> Unit,
    dismiss: () -> Unit
) {
    val viewModel = getViewModel(key = CardSelectViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { CardSelectViewModel(list) })
    val state: CardSelectState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        backKeyListener = {
            if (state.screenState == CardSelectState.CARD_SELECT_DETAIL_SCREEN) {
                viewModel.selectScreen(CardSelectState.CARD_SELECT_DEFAULT_SCREEN)
            } else {
                dismiss.invoke()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Gray).padding(30.dp)) {
        Column(
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)).border(
                width = 2.dp, color = LightColorScheme.primary, shape = RoundedCornerShape(12.dp)
            ).background(Color.White), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단에 검색과 필터 뷰 영역
            CardSelectSearchBar(state.search,
                state.sortFilter,
                state.isReverseFilter,
                { viewModel.toggleReverseFilter() }) { searchKeyword: String, index: Int ->
                viewModel.searchSortList(searchKeyword, index)
            }

            Column(modifier = Modifier.padding(6.dp)) {
                if (selectMax > 0) {
                    Text(
                        text = "${state.selectedCardList.size}/$selectMax",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black
                        )
                    )
                }

                if (state.selectedCardList.isNotEmpty()) {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1), modifier = Modifier.weight(1.2f)
                    ) {
                        items(state.selectedCardList.size) {
                            val card = state.selectedCardList[it]
                            CardListSmallItemScreen(card,
                                height = 160f,
                                visibleInfoType = state.sortFilter,
                                onItemDetailInfoClick = {
                                    viewModel.selectScreen(
                                        CardSelectState.CARD_SELECT_DETAIL_SCREEN, card
                                    )
                                }) { selectedCard ->
                                if (selectedCard != null) {
                                    viewModel.selectItem(selectedCard)
                                }
                            }
                        }
                    }
                } else {
                    if (selectMax > 0) {
                        Text(
                            text = "선택한 카드가 없습니다.",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold, fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))
                LazyVerticalGrid(modifier = Modifier.weight(3f), columns = GridCells.Fixed(2)) {
                    items(state.sortedCardList.size) {
                        val card = state.sortedCardList[it]
                        CardListSmallItemScreen(card,
                            height = 200f,
                            visibleInfoType = state.sortFilter,
                            selected = state.selectedCardList.contains(card),
                            onItemDetailInfoClick = {
                                viewModel.selectScreen(
                                    CardSelectState.CARD_SELECT_DETAIL_SCREEN, card
                                )
                            }

                        ) { selectedCard ->
                            if (selectedCard != null) {
                                if (selectMax > 0) {
                                    if (state.selectedCardList.size < selectMax) {
                                        viewModel.selectItem(selectedCard)
                                    }
                                } else {
                                    selectListener.invoke(arrayListOf(selectedCard))
                                    dismiss.invoke()
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                    Button(modifier = Modifier.weight(1f), onClick = {
                        dismiss.invoke()
                    }) {
                        Text(text = getString(SharedRes.strings.cancel))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    if (selectMax > 0) {
                        val btnEnable =
                            if (isForceMaxSelect) state.selectedCardList.size >= selectMax else state.selectedCardList.isNotEmpty()
                        Button(enabled = btnEnable, modifier = Modifier.weight(1f), onClick = {
                            selectListener.invoke(state.selectedCardList)
                            dismiss.invoke()
                        }) {
                            Text(text = getString(SharedRes.strings.confirm))
                        }
                    }
                }
            }
        }
        if (state.screenState == CardSelectState.CARD_SELECT_DETAIL_SCREEN) {
            state.detailCardInfo?.let {
                Box(modifier = Modifier.fillMaxSize().background(Color.White))
                CardDetailInfoScreen(card = it) {
                    viewModel.selectScreen(CardSelectState.CARD_SELECT_DEFAULT_SCREEN)
                }
            }
        }
    }
}