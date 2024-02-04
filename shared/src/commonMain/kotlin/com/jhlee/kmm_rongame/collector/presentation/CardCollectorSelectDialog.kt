package com.jhlee.kmm_rongame.collector.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.jhlee.kmm_rongame.cardselector.presentaion.CardSelectDialog
import com.jhlee.kmm_rongame.collector.domain.CardCollectorWantedItem
import com.jhlee.kmm_rongame.core.util.Logger

@Composable
fun CardCollectorSelectDialog(
    viewModel: CardCollectorViewModel, item: CardCollectorWantedItem, dismiss: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
        .pointerInput(Unit) {
            detectTapGestures { }
        }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                CardSelectDialog(state.selectList, item.count, true, {
                    viewModel.sellWantedCard(it, item)
                }, dismiss)
            }
        }
    }
}