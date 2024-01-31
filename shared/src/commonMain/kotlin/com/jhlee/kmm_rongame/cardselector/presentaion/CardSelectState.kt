package com.jhlee.kmm_rongame.cardselector.presentaion

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.constants.CardFilterConst

data class CardSelectState(
    val isLoading: Boolean = false,
    val screenState: Int = 0,
    val originCardList: List<Card> = emptyList(),
    val detailCardInfo: Card? = null,
    val sortedCardList: List<Card> = emptyList(),
    val selectedCardList: List<Card> = emptyList(),
    val search: String = "",
    val sortFilter: Int = CardFilterConst.POWER,
    val isReverseFilter: Boolean = false
) {
    companion object {
        const val CARD_SELECT_DEFAULT_SCREEN = 0
        const val CARD_SELECT_DETAIL_SCREEN = 1
    }
}