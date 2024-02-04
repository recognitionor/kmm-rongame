package com.jhlee.kmm_rongame.collector.presentation

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.coin.domain.Coin
import com.jhlee.kmm_rongame.collector.domain.CardCollectorWantedItem

data class CardCollectorState(
    val isLoading: Boolean = false,
    val commentIndex: Int = 0,
    val error: String = "",
    val selectedCardCollectorWantedItem: CardCollectorWantedItem? = null,
    val cardCollectorWantedItemList: List<CardCollectorWantedItem> = emptyList(),
    val selectList: List<Card> = emptyList(),
    val screenMode: Int = DEFAULT_SCREEN
) {
    companion object {
        const val DEFAULT_SCREEN = 0
        const val CARD_SELECT_SCREEN = 1
        const val CARD_WASTE_SELECT_SCREEN = 2
    }
}