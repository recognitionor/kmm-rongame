package com.jhlee.kmm_rongame.card.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getString

@Composable
fun CardListScreen(homeViewModel: HomeViewModel) {
    val cardStateValue by homeViewModel.state.collectAsState()

    Column {
        Text(
            text = "${getString(SharedRes.strings.card_list_title)} : ${cardStateValue.cardList.size} 개",
            fontSize = 24.sp,
            style = TextStyle(
                // 폰트
                //fontFamily = FontFamily(Font(Font.))
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(cardStateValue.cardList.size) { index ->
                val card = cardStateValue.cardList[cardStateValue.cardList.size - index - 1]
                Row(horizontalArrangement = Arrangement.Center) {
                    CardListItemScreen(card = card,
                        visibleInfoType = cardStateValue.sortFilter,
                        height = 200f,
                        onItemDetailInfoClick = { homeViewModel.toggleCardInfoDialog(card) }) {
                        homeViewModel.toggleCardInfoDialog(card)
                    }
                }
            }
        }
    }

}
