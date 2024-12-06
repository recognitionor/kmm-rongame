package com.jhlee.kmm_rongame.card.presentation

import com.jhlee.kmm_rongame.card.data.CardCombinationInfo
import com.jhlee.kmm_rongame.card.domain.Card

data class CardCombinationState(
    val isLoading: Boolean = false,
    val isLoadDone: Boolean = false,
    val animationMode: Int = ANIMATION_DEFAULT,
    val combineCard: Card? = null,
    val myCardList: List<Card> = emptyList(),
    val cardCombinationInfo: List<CardCombinationInfo> = emptyList(),
    val mySelectedCardEntry: List<Card?> = List(2) { null },
    val error: String = "",
    val cardRandomProgress: Int = 0,
    val homeScreenMode: Int = 0,
    val isCombining: Boolean = false
) {
    companion object {
        const val CARD_COMBINE_DEFAULT = 0
        const val CARD_COMBINE_READY = 1
        const val CARD_COMBINE_DONE = 2

        const val ANIMATION_DEFAULT = -1
        const val ANIMATION_FAIL = 0
        const val ANIMATION_SUCCESS = 1

    }
}
