package com.jhlee.kmm_rongame.cardgame.presentaion

import com.jhlee.kmm_rongame.card.domain.Card

data class CardGameState(
    val isLoading: Boolean = false,

    val screenMode: Int = CardGameViewModel.CARD_GAME_DEFAULT_SCREEN,

    val selectStageIndex: Int = -1,

    val selectCardIndex: Int = -1,

    val cardGameResult: String = "",

    val myFinalPower: Int = -1,

    val enemyFinalPower: Int = -1,
    // list 에서 0적 1 이 my
    val resultMap: Map<Int, List<Int>> = emptyMap(),

    val enemySpareEntry: List<Card> = emptyList(),

    val enemyEntry: List<Card?> = emptyList(),

    val mySelectedCardEntry: List<Card?> = List(3) { null },

    val myCardEntry: List<Card> = emptyList(),

    val gameState: Int = CARD_GAME_STATE_PRE


) {
    companion object {
        const val CARD_GAME_STATE_PRE = 0
        const val CARD_GAME_STATE_ING = 1
        const val CARD_GAME_STATE_OPEN_CARD = 2
        const val CARD_GAME_STATE_OPEN_CARD_RESULT = 3
    }
}