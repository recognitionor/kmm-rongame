package com.jhlee.kmm_rongame.pandora.presentation

import com.jhlee.kmm_rongame.card.domain.Card

data class PandoraState(
    val pandoraState: Int = STATE_DEFAULT,
    val cardList: List<Card?> = emptyList(),
    val startSelectedIndex: Int = -1,
    val afterSelectedIndex: Int = -1,
    val detailCardInfo: Card? = null,
    val cardGatchaLoading: Int = -1,
    val cardGatchaIndex: Int = -1,
    val isSelectMode: Boolean = false,
    val detailCard: Card? = null,
    val cardListSize: Int = 0,
    val rowSize: Int = 0,
    val colSize: Int = 0,
    val goalCard: Card? = null,
    val openCardCount: Int = 0,
    val upgradeCount: Int = 0,
) {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_GAME_OVER = 1
        const val STATE_GAME_WIN = 2
        const val STATE_GAME_WIN_PICK = 3
        const val PICK_DONE = 4
    }
}