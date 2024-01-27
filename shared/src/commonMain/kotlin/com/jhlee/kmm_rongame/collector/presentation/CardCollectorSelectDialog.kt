package com.jhlee.kmm_rongame.collector.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.card.presentation.CardListSmallItemScreen

@Composable
fun CardCollectorSelectDialog(viewModel: CardCollectorViewModel, dismiss: () -> Unit) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
        .pointerInput(Unit) {
            detectTapGestures { }
        }) {
        Column(
            modifier = Modifier.padding(50.dp).fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Column(modifier = Modifier.padding(12.dp)) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.weight(9f)) {
                        items(state.selectList.size) {
                            val card = state.selectList[it]
                            CardListSmallItemScreen(card,
                                height = 160f,
                                onItemDetailInfoClick = {}) {

                            }
                        }
                    }

                    Button(modifier = Modifier.weight(0.5f).fillMaxWidth(), onClick = {
                        dismiss.invoke()
                    }) {
                        Text(text = "확인")
                    }
                }
            }

        }
    }
}