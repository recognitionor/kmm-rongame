package com.jhlee.kmm_rongame.pandora.presentation

import com.jhlee.kmm_rongame.card.domain.Card

data class PandoraState(
    val cardList: List<Card?> = emptyList(),
    val startSelectedIndex: Int = -1,
    val afterSelectedIndex: Int = -1,
    val detailCardInfo: Card? = null,
    val cardGatchaLoading: Int = -1,
    val cardGatchaIndex: Int = -1,
    val isSelectMode: Boolean = false,
    val detailCard: Card? = null
)