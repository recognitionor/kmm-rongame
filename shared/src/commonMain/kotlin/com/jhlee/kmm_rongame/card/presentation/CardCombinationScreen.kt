package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch

@Composable
fun CardCombinationScreen(appModule: AppModule, dismiss: () -> Unit) {
    val viewModel = getViewModel(key = CardCombinationViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { CardCombinationViewModel(appModule.dbCardCombinationDataSource) })
    val state by viewModel.state.collectAsState()

    var selectCardSlot by remember { mutableStateOf(-1) }
    var showDialog by remember { mutableStateOf(false) }
    val offsetVanish by remember { mutableStateOf(Animatable(1f)) }
    val offsetAppear by remember { mutableStateOf(Animatable(0f)) }
    var cardOpen by remember { mutableStateOf(false) }
    val offsetX by remember { mutableStateOf(Animatable(0f)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state, state.animationMode) {
        when (state.animationMode) {
            CardCombinationState.ANIMATION_FAIL -> {
                scope.launch {
                    var count = 0
                    while (true) {
                        offsetX.animateTo(10f, animationSpec = tween(50))
                        offsetX.animateTo(0f, animationSpec = tween(50))
                        if (count > 1) {
                            viewModel.clearAnimation()
                            break
                        }
                        count++
                    }
                }
            }

            CardCombinationState.ANIMATION_SUCCESS -> {
                scope.launch {
                    var count = 0
                    while (true) {
                        offsetVanish.animateTo(0f, animationSpec = tween(1000))
                        cardOpen = true
                        offsetAppear.animateTo(1f, animationSpec = tween(1000))
                        if (count > 1) {
                            viewModel.clearAnimation()
                            break
                        }
                        count++
                    }
                }
            }
        }
        viewModel.clearAnimation()
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

            Text(
                text = "카드 결합",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp, fontWeight = FontWeight.Bold
                )
            )
        }
    }) {

        Box(modifier = Modifier.padding(top = 50.dp)) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    if (state.combineCard == null || !cardOpen) {
                        LazyRow {
                            items(2) { index ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.alpha(offsetVanish.value)
                                ) {
                                    CardListSmallItemScreen(state.mySelectedCardEntry[index],
                                        height = 160f,
                                        onItemDetailInfoClick = {
                                        }) {
                                        selectCardSlot = index
                                        showDialog = true
                                    }
                                    if (state.mySelectedCardEntry[index] != null) {
                                        Image(imageVector = Icons.Default.Clear,
                                            contentDescription = "Account",
                                            modifier = Modifier.clickable {
                                                viewModel.clearSelectedCard(index)
                                            } // 클릭 이벤트 처리
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(modifier = Modifier.alpha(offsetAppear.value)) {
                            CardListSmallItemScreen(
                                state.combineCard,
                                height = 160f,
                                onItemDetailInfoClick = {
                                }) {

                            }
                        }
                        viewModel.clearSelected()
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                if (!state.mySelectedCardEntry.contains(null)) {
                    Column(
                        modifier = Modifier.offset(x = offsetX.value.dp).clickable {
                            viewModel.combinationCard()
                        }, horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        getCommonImageResourceBitMap(SharedRes.images.ic_combine)?.let { bitmap ->
                            Image(
                                bitmap = bitmap,
                                contentDescription = "",
                            )
                        }
                        Text("합체(Combine)")
                    }

                }

                if (state.error.isNotEmpty()) {
                    Text(text = state.error)
                }
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
                        val filteredList = state.myCardList.filter { card ->
                            val isCardInSelectedList =
                                state.mySelectedCardEntry.any { it?.id == card.id }
                            !isCardInSelectedList
                        }

                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(filteredList.size) { index ->
                                CardListItemScreen(filteredList[(filteredList.size - 1) - index],
                                    height = 160f,
                                    onItemDetailInfoClick = {
//                                    cardDetailView = it
                                    }) {
                                    viewModel.selectMyCard(selectCardSlot, it)
                                    showDialog = false
                                }
                            }
                        }
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = {
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
    }
}
