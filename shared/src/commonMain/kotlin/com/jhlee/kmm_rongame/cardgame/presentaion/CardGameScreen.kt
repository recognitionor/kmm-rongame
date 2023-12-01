package com.jhlee.kmm_rongame.cardgame.presentaion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.presentation.CardDetailInfoScreen
import com.jhlee.kmm_rongame.card.presentation.CardListItemScreen
import com.jhlee.kmm_rongame.card.presentation.CardListSmallItemScreen
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule

@Composable
fun CardGameScreen(
    appModule: AppModule, viewModel: CardGameViewModel, state: CardGameState, dismiss: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectCardSlot by remember { mutableStateOf(-1) }
    var cardDetailView by remember { mutableStateOf<Card?>(null) }
    var enemyScore = 0
    var myScore = 0
    state.resultMap.forEach {
        if (it.value[0] > it.value[1]) {
            enemyScore++
        } else if (it.value[0] < it.value[1]) {
            myScore++
        }
    }

    Scaffold(topBar = {
        Row {
            getCommonImageResourceBitMap(SharedRes.images.ic_back)?.let {
                Image(
                    bitmap = it, contentDescription = null, modifier = Modifier.clickable {
                        viewModel.exitGame()
                        dismiss.invoke()
                    }.width(30.dp).height(30.dp).padding(5.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${state.selectStageIndex + 1} 단계",
                fontSize = 24.sp,
                color = Color.DarkGray,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer(modifier = Modifier.weight(1f))
        }

    }) {
        getCommonImageResourceBitMap(SharedRes.images.ic_enemy)?.let { it1 ->
            Image(
                modifier = Modifier.fillMaxSize().alpha(0.4f),
                bitmap = it1,
                contentDescription = null
            )
            Column {
                Spacer(modifier = Modifier.height(40.dp))
                Box(modifier = Modifier.weight(1f)) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val enemyList = if (state.gameState == CardGameState.CARD_GAME_STATE_PRE) {
                            state.enemySpareEntry
                        } else {
                            state.enemyEntry
                        }

                        items(enemyList.size) { index ->
                            val result = state.resultMap[index]
                            var resultEnemyString = ""
                            if (result != null) {
                                resultEnemyString = if (result[0] > result[1]) {
                                    "Win"
                                } else if (result[0] < result[1]) {
                                    "Lose"
                                } else {
                                    "Draw"
                                }
                            }
                            Box(modifier = Modifier.align(Alignment.Center)) {
                                CardListSmallItemScreen(enemyList[index],
                                    height = 160f,
                                    infoMsg = "Open!!",
                                    onItemDetailInfoClick = {
                                        cardDetailView = it
                                    }) {
                                    if (state.gameState == CardGameState.CARD_GAME_STATE_OPEN_CARD || state.gameState == CardGameState.CARD_GAME_STATE_OPEN_CARD_RESULT) {
                                        viewModel.openCard(index)
                                    } else {
                                        cardDetailView = it
                                    }
                                }
                                if (resultEnemyString.isNotEmpty()) {
                                    Text(
                                        text = resultEnemyString,
                                        modifier = Modifier.alpha(0.8f).align(
                                            Alignment.Center
                                        ),
                                        fontSize = 50.sp,
                                        color = Color.Red,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontFamily = FontFamily.Default,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    Column(verticalArrangement = Arrangement.Center) {
                        when (state.gameState) {
                            CardGameState.CARD_GAME_STATE_PRE -> {
                                Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                                    Text(
                                        "상대의 카드 6개중에 3개가 뽑히게 됩니다.",
                                        fontSize = 18.sp,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontFamily = FontFamily.Default,
                                            fontWeight = FontWeight.Light
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Button(
                                        onClick = {
                                            viewModel.startGame()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = !state.mySelectedCardEntry.contains(null)
                                    ) {
                                        Text(text = getString(SharedRes.strings.start))
                                    }
                                }
                            }

                            CardGameState.CARD_GAME_STATE_OPEN_CARD_RESULT -> {
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = "$enemyScore",
                                                modifier = Modifier.align(Alignment.Center),
                                                style = TextStyle(
                                                    fontSize = 50.sp,
                                                    fontFamily = FontFamily.Default,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "상대 카드",
                                            color = Color.Red,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontFamily = FontFamily.Default,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        CardListSmallItemScreen(
                                            state.enemyEntry[state.selectCardIndex],
                                            height = 160f,
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "결과",
                                            color = Color.Black,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontFamily = FontFamily.Default,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = state.cardGameResult,
                                            color = Color.Black,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily.Default,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                modifier = Modifier.weight(1f),
                                                text = state.enemyFinalPower.toString(),
                                                color = Color.Red,
                                                textAlign = TextAlign.Center,
                                                style = TextStyle(
                                                    fontSize = 30.sp,
                                                    fontFamily = FontFamily.Default,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Text(
                                                modifier = Modifier.weight(1f),
                                                text = state.myFinalPower.toString(),
                                                color = Color.Green,
                                                textAlign = TextAlign.Center,
                                                style = TextStyle(
                                                    fontSize = 30.sp,
                                                    fontFamily = FontFamily.Default,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = "$myScore",
                                                modifier = Modifier.align(Alignment.Center),
                                                style = TextStyle(
                                                    fontSize = 50.sp,
                                                    fontFamily = FontFamily.Default,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = "내 카드",
                                            color = Color.Green,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontFamily = FontFamily.Default,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )


                                        CardListSmallItemScreen(
                                            state.mySelectedCardEntry[state.selectCardIndex],
                                            height = 160f,
                                        )
                                    }
                                }
                            }

                            else -> {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    Row {
                        repeat(3) { count ->
                            val result = state.resultMap[count]
                            var resultString = ""
                            if (result != null) {
                                resultString = if (result[0] < result[1]) {
                                    "Win"
                                } else if (result[0] > result[1]) {
                                    "Lose"
                                } else {
                                    "Draw"
                                }
                            }
                            Box(contentAlignment = Alignment.Center) {

                                CardListSmallItemScreen(state.mySelectedCardEntry[count],
                                    height = 160f,
                                    onItemDetailInfoClick = {
                                        cardDetailView = it
                                    }) {
                                    if (state.gameState == CardGameState.CARD_GAME_STATE_PRE) {
                                        showDialog = true
                                        selectCardSlot = count
                                    }
                                }
                                if (resultString.isNotEmpty()) {
                                    Text(
                                        text = resultString,
                                        fontSize = 50.sp,
                                        color = Color.Green,
                                        textAlign = TextAlign.Center,
                                        style = TextStyle(
                                            fontFamily = FontFamily.Default,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (state.resultMap.size >= 3) {
        Column(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
            .pointerInput(Unit) {
                detectTapGestures { }
            }) {

            Spacer(modifier = Modifier.weight(1f))

            var resultStr = ""
            resultStr = if (enemyScore < myScore) {
                "승"
            } else if (enemyScore > myScore) {
                "패"
            } else {
                "무승부"
            }

            Column(
                modifier = Modifier.background(Color.White).fillMaxWidth()
                    .align(Alignment.CenterHorizontally).padding(start = 20.dp, end = 20.dp)
            ) {
                Text(
                    resultStr,
                    fontSize = 50.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = {
                    viewModel.exitGame()
                    dismiss.invoke()
                }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        text = getString(SharedRes.strings.confirm),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

        }
    }

    if (showDialog) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
                .pointerInput(Unit) {
                    detectTapGestures { }
                }, contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                Column(modifier = Modifier.background(Color.White).padding(24.dp)) {
                    Text(text = "카드를 선택 해주세요")
                    val filteredList = state.myCardEntry.filter { card ->
                        val isCardInSelectedList =
                            state.mySelectedCardEntry.any { it?.id == card.id }
                        !isCardInSelectedList
                    }
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(filteredList.size) { index ->
                            CardListItemScreen(
                                filteredList[index],
                                height = 160f,
                                onItemDetailInfoClick = {
                                    cardDetailView = it
                                }) {
                                viewModel.selectMyCard(selectCardSlot, it)
                                showDialog = false
                            }
                        }
                    }
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(onClick = {
                            selectCardSlot = 0
                            showDialog = false
                        }) {
                            Text(text = "취소")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }

                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
    cardDetailView?.let {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White).pointerInput(Unit) {
                detectTapGestures { }
            }, contentAlignment = Alignment.Center
        ) {
            CardDetailInfoScreen(it) {
                cardDetailView = null
            }
        }
    }

}

